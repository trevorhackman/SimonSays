package hackman.trevor.copycat.ui.remove_ads

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import hackman.trevor.copycat.R
import hackman.trevor.copycat.logic.remove_ads.Product
import hackman.trevor.copycat.logic.viewmodels.RemoveAdsViewModel
import hackman.trevor.copycat.observe

class RemoveAdsButtonLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : ConstraintLayout(context, attributeSet), LifecycleOwner {

    private lateinit var removeAdsViewModel: RemoveAdsViewModel
    private lateinit var lifecycle: Lifecycle

    private val buttons: List<RemoveAdsProductButton>
        get() = listOf(
            findViewById(R.id.remove_ads_product_1),
            findViewById(R.id.remove_ads_product_2),
            findViewById(R.id.remove_ads_product_3),
            findViewById(R.id.remove_ads_product_4)
        )

    init {
        View.inflate(context, R.layout.remove_ads_buttons, this)
    }

    fun setup(removeAdsViewModel: RemoveAdsViewModel, lifecycle: Lifecycle) {
        this.removeAdsViewModel = removeAdsViewModel
        this.lifecycle = lifecycle
        setupButtons()
        observePrices()
    }

    private fun setupButtons() = buttons.forEachIndexed { index, button ->
        button.setup(removeAdsViewModel, Product.values()[index])
    }

    private fun observePrices() = observe(removeAdsViewModel.prices) {
        buttons[0].text = it.price1
        buttons[1].text = it.price2
        buttons[2].text = it.price3
        buttons[3].text = it.price4
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        buttons.forEach {
            it.isEnabled = enabled
        }
    }

    // Find max width of buttons and set to all buttons for a consistent width
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val buttonWidth = findMaxButtonWidth()
        for (button in buttons) button.width = buttonWidth
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    private fun findMaxButtonWidth(): Int = buttons.map {
        val widthSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        val heightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        it.measure(widthSpec, heightSpec)
        it.measuredWidth
    }.maxOrNull()!!

    override fun getLifecycle() = lifecycle
}
