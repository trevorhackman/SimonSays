package hackman.trevor.copycat.ui.color_button

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import hackman.trevor.copycat.system.displayMinimum
import hackman.trevor.copycat.system.pixelTextSize

// TODO Would be nice to improve theoretical performance, repeating the same animation & measurement calculations four times for the four instances of InfoText, could eliminate repeat calculations
class InfoText @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatTextView(context, attributeSet) {

    private val placeholder = TextView(context)

    private var fullHeight = 0
    private var interpolatedHeight = 0

    var shrinked = true
        set(value) {
            if (field == value) return
            if (value) shrink()
            else unshrink()
            field = value
        }

    init {
        pixelTextSize = displayMinimum() * .05f
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // TODO Do we need to measure this everytime? Could improve theoretical performance again here
        measureFullHeight(widthMeasureSpec)

        // Use interpolated height for cool animation
        setMeasuredDimension(widthMeasureSpec, interpolatedHeight)
    }

    // Measure would-be height with wrap_content
    private fun measureFullHeight(widthMeasureSpec: Int) {
        placeholder.layoutParams = layoutParams
        placeholder.pixelTextSize = textSize
        placeholder.text = text

        val widthSpec = MeasureSpec.makeMeasureSpec(widthMeasureSpec, MeasureSpec.EXACTLY)
        val wrapHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        placeholder.measure(widthSpec, wrapHeightSpec)
        if (placeholder.measuredHeight > 0) fullHeight = placeholder.measuredHeight
    }

    private fun shrink() {
        nonstandardAnimate().setDuration(slideAnimationDuration).setUpdateListener {
            interpolatedHeight = (fullHeight * (1 - it.animatedFraction)).toInt()
            requestLayout()
            invalidate()
        }
    }

    private fun unshrink() {
        nonstandardAnimate().setDuration(slideAnimationDuration).setUpdateListener {
            interpolatedHeight = (fullHeight * it.animatedFraction).toInt()
            requestLayout()
            invalidate()
        }
    }

    // Pointless alpha animation done b/c ViewPropertyAnimator was written to not do anything without any 'standard' property
    private fun nonstandardAnimate() = animate().alpha(this.alpha)

    companion object {
        private const val slideAnimationDuration = 800L
    }
}
