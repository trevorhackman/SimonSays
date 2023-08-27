package hackman.trevor.copycat.ui.remove_ads

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import hackman.trevor.billing.Billing
import hackman.trevor.billing.Ownership
import hackman.trevor.copycat.databinding.RemoveAdsMenuBinding
import hackman.trevor.copycat.logic.viewmodels.RemoveAdsViewModel
import hackman.trevor.copycat.observe
import hackman.trevor.copycat.system.displayHeight
import hackman.trevor.copycat.system.displayWidth
import hackman.trevor.copycat.ui.fadeIn
import hackman.trevor.copycat.ui.fadeOut
import kotlin.math.min

class RemoveAdsMenu @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : FrameLayout(context, attributeSet), LifecycleOwner {

    private lateinit var removeAdsViewModel: RemoveAdsViewModel
    override lateinit var lifecycle: Lifecycle

    private val binding = RemoveAdsMenuBinding.inflate(LayoutInflater.from(context), this, true)

    fun setup(removeAdsViewModel: RemoveAdsViewModel, lifecycle: Lifecycle) {
        this.removeAdsViewModel = removeAdsViewModel
        this.lifecycle = lifecycle
        setupButtons()
        setupCloseButton()
        observeInBackground()
        observeBillingOwnership()
    }

    private fun setupButtons() = binding.adsMenuButtons.setup(removeAdsViewModel, lifecycle)

    private fun setupCloseButton() = binding.adsMenuCloseButton.setOnClickListener {
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
        binding.adsMenuCloseButton.isEnabled = enabled
        binding.adsMenuButtons.isEnabled = enabled
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.makeMeasureSpec(determineWidth(), MeasureSpec.EXACTLY)
        super.onMeasure(width, heightMeasureSpec)
    }

    private fun determineWidth() = min(displayWidth(), (displayWidth() + displayHeight()) / 2)
}
