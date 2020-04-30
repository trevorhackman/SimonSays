package hackman.trevor.copycat.ui.main

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import hackman.trevor.copycat.system.ads.AdManager

class AdContainer @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : FrameLayout(context, attributeSet) {

    private lateinit var adManager: AdManager

    private var initial = true

    private var previousWidthMeasureSpec = 0
    private var previousHeightMeasureSpec = 0

    private var onMeasureWidthMeasureSpec = 0
    private var onMeasureHeightMeasureSpec = 0

    fun setup(adManager: AdManager) {
        this.adManager = adManager
        addView(adManager.getBannerAd())
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        onMeasureWidthMeasureSpec = widthMeasureSpec
        onMeasureHeightMeasureSpec = heightMeasureSpec
        if (initial) onInitial()
        else if (screenSizeChanged()) onScreenSizeChanged()
    }

    private fun onInitial() {
        rememberSize()
        initial = false
    }

    private fun rememberSize() {
        previousWidthMeasureSpec = onMeasureWidthMeasureSpec
        previousHeightMeasureSpec = onMeasureHeightMeasureSpec
    }

    private fun screenSizeChanged() =
        previousWidthMeasureSpec != onMeasureWidthMeasureSpec || previousHeightMeasureSpec != onMeasureHeightMeasureSpec

    private fun onScreenSizeChanged() {
        rememberSize()
        adManager.buildBannerAd()
        addView(adManager.getBannerAd())
    }
}
