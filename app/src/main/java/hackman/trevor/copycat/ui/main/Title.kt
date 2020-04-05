package hackman.trevor.copycat.ui.main

import android.animation.TimeInterpolator
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import hackman.trevor.copycat.R
import kotlin.math.min

class Title @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatImageView(context, attributeSet) {
    private val interpolator: TimeInterpolator = TimeInterpolator { v ->
        -2 * v * v + 3 * v // Allows me two animations in one! Peaks at v = 0.75 then down to (1,1)
    }

    init {
        setImageResource(R.drawable.title)
    }

    fun popIn() {
        animate()
            .scaleX(1.0f)
            .scaleY(1.0f)
            .alpha(1.0f)
            .setDuration(titlePopDuration.toLong())
            .interpolator = interpolator
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        if (width != 0 && height != 0) determineSize(width, height)
    }

    private fun determineSize(width: Int, height: Int) {
        val layoutParams = layoutParams
        layoutParams.width = if (isPortrait(width, height)) width else min((width + 3 * height) / 4, width)
        this.layoutParams = layoutParams
        context.resources.displayMetrics.widthPixels
        context.resources.displayMetrics.heightPixels
    }

    private fun isPortrait(width: Int, height: Int) = width <= height

    companion object {
        private const val titlePopDuration = 1100
    }
}
