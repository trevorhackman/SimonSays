package hackman.trevor.copycat.ui.main

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import hackman.trevor.copycat.system.ads.AdManager

class AdContainer @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : FrameLayout(context, attributeSet) {

    private var initial = true

    private var previousWidthMeasureSpec = 0
    private var previousHeightMeasureSpec = 0

    private var onMeasureWidthMeasureSpec = 0
    private var onMeasureHeightMeasureSpec = 0

    init {
        if (AdManager.isEnabled) addView(AdManager.getBannerAd())
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
        if (childCount == 0) addView(AdManager.getBannerAd())
    }

    private fun rememberSize() {
        previousWidthMeasureSpec = onMeasureWidthMeasureSpec
        previousHeightMeasureSpec = onMeasureHeightMeasureSpec
    }

    private fun screenSizeChanged() =
        previousWidthMeasureSpec != onMeasureWidthMeasureSpec || previousHeightMeasureSpec != onMeasureHeightMeasureSpec

    private fun onScreenSizeChanged() {
        rememberSize()
        AdManager.buildBannerAd()
        addView(AdManager.getBannerAd())
    }
}
