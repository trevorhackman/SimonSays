package hackman.trevor.copycat.ui.remove_ads

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import hackman.trevor.billing.Billing
import hackman.trevor.billing.Ownership
import hackman.trevor.copycat.R
import hackman.trevor.copycat.logic.viewmodels.RemoveAdsViewModel
import hackman.trevor.copycat.observe
import hackman.trevor.copycat.system.displayHeight
import hackman.trevor.copycat.system.displayWidth
import hackman.trevor.copycat.ui.fadeIn
import hackman.trevor.copycat.ui.fadeOut
import kotlinx.android.synthetic.main.remove_ads_menu.view.*
import kotlin.math.min

class RemoveAdsMenu @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : ConstraintLayout(context, attributeSet), LifecycleOwner {

    private lateinit var removeAdsViewModel: RemoveAdsViewModel
    private lateinit var lifecycle: Lifecycle

    init {
        View.inflate(context, R.layout.remove_ads_menu, this)
    }

    fun setup(removeAdsViewModel: RemoveAdsViewModel, lifecycle: Lifecycle) {
        this.removeAdsViewModel = removeAdsViewModel
        this.lifecycle = lifecycle
        setupButtons()
        setupCloseButton()
        observeInBackground()
        observeBillingOwnership()
    }

    private fun setupButtons() = ads_menu_buttons.setup(removeAdsViewModel, lifecycle)

    private fun setupCloseButton() = ads_menu_close_button.setOnClickListener {
        removeAdsViewModel.closeClicked()
    }

    private fun observeInBackground() = observe(removeAdsViewModel.inBackground) {
        if (it) fadeOut()
        else fadeIn(startAction = { removeAdsViewModel.isAnimatingIn = true }) {
            removeAdsViewModel.isAnimatingIn = false
        }
    }

    // Close ad menu when purchase is made
    private fun observeBillingOwnership() = observe(Billing.liveData.ownership) {
        if (it == Ownership.Owned) removeAdsViewModel.setInBackground(true)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        ads_menu_close_button.isEnabled = enabled
        ads_menu_buttons.isEnabled = enabled
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.makeMeasureSpec(determineWidth(), MeasureSpec.EXACTLY)
        super.onMeasure(width, heightMeasureSpec)
    }

    private fun determineWidth() = min(displayWidth(), (displayWidth() + displayHeight()) / 2)

    override fun getLifecycle() = lifecycle
}
