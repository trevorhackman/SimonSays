package hackman.trevor.copycat.ui.main

import android.animation.TimeInterpolator
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import hackman.trevor.copycat.R
import hackman.trevor.copycat.system.displayHeight
import hackman.trevor.copycat.system.displayWidth
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

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.makeMeasureSpec(determineWidth(), MeasureSpec.EXACTLY)
        super.onMeasure(width, heightMeasureSpec)
    }

    private fun determineWidth(): Int =
        min((displayWidth() + 3 * displayHeight()) / 4, displayWidth())

    fun popIn() {
        animate()
            .scaleX(1.0f)
            .scaleY(1.0f)
            .alpha(1.0f)
            .setDuration(titlePopDuration.toLong())
            .interpolator = interpolator
    }

    companion object {
        private const val titlePopDuration = 1100
    }
}
