package hackman.trevor.copycat.ui.main

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import hackman.trevor.billing.Billing
import hackman.trevor.billing.Ownership
import hackman.trevor.copycat.observe
import hackman.trevor.copycat.system.ads.AdManager

class AdContainer @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : FrameLayout(context, attributeSet), LifecycleOwner {

    override lateinit var lifecycle: Lifecycle

    private var initial = true

    private var previousWidthMeasureSpec = 0
    private var previousHeightMeasureSpec = 0

    private var onMeasureWidthMeasureSpec = 0
    private var onMeasureHeightMeasureSpec = 0

    init {
        if (AdManager.isEnabled) AdManager // addView(AdManager.getBannerAd())
    }

    fun setup(lifecycle: Lifecycle) {
        this.lifecycle = lifecycle
        observeBillingOwnership()
    }

    // Remove banner ads from current session when purchase is made
    private fun observeBillingOwnership() = observe(Billing.liveData.ownership) {
        if (it == Ownership.Owned) {
            removeAllViews()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (AdManager.isEnabled) {
            onMeasureWidthMeasureSpec = widthMeasureSpec
            onMeasureHeightMeasureSpec = heightMeasureSpec
            if (initial) onInitial()
            else if (screenSizeChanged()) onScreenSizeChanged()
        }
    }

    private fun onInitial() {
        rememberSize()
        initial = false
        if (childCount == 0) AdManager // addViewIfNotNull(AdManager.getBannerAd())
    }

    private fun rememberSize() {
        previousWidthMeasureSpec = onMeasureWidthMeasureSpec
        previousHeightMeasureSpec = onMeasureHeightMeasureSpec
    }

    private fun screenSizeChanged() =
        previousWidthMeasureSpec != onMeasureWidthMeasureSpec || previousHeightMeasureSpec != onMeasureHeightMeasureSpec

    private fun onScreenSizeChanged() {
        rememberSize()
        // AdManager.buildBannerAd()
        // addViewIfNotNull(AdManager.getBannerAd())
    }

    private fun addViewIfNotNull(view: View?) {
        view?.let { addView(it) }
    }
}
