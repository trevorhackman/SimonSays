package hackman.trevor.copycat.ui.color_button

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import hackman.trevor.copycat.system.displayMinimum
import hackman.trevor.copycat.system.pixelTextSize
import kotlin.properties.Delegates

class InfoText @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatTextView(context, attributeSet) {

    private val placeholder = TextView(context)

    private var fullHeight: Int by Delegates.notNull()
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
        // Perform one pass to get width for placeholder
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        measurePlaceholderHeight()

        // Keep measured width, use interpolated height for cool animation
        setMeasuredDimension(measuredWidth, interpolatedHeight)
    }

    // Measure would-be height with wrap_content
    private fun measurePlaceholderHeight() {
        placeholder.layoutParams = layoutParams
        placeholder.pixelTextSize = textSize
        placeholder.text = text

        val widthSpec = MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY)
        val wrapHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        placeholder.measure(widthSpec, wrapHeightSpec)
        if (placeholder.measuredHeight > 0) fullHeight = placeholder.measuredHeight
    }

    private fun shrink() {
        val fullHeight = this.fullHeight // Cache b/c it changes during animation
        animate().alpha(1f).setDuration(800).setUpdateListener {
            interpolatedHeight = fullHeight - (fullHeight * it.animatedFraction).toInt()
            height = interpolatedHeight
        }
    }

    private fun unshrink() {
        val fullHeight = this.fullHeight // Cache b/c it changes during animation
        animate().alpha(1f).setDuration(800).setUpdateListener {
            interpolatedHeight = (fullHeight * it.animatedFraction).toInt()
            height = interpolatedHeight
        }
    }
}
