package hackman.trevor.copycat.ui.settings

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import hackman.trevor.copycat.system.displayMinimum
import hackman.trevor.copycat.system.pixelTextSize

class SettingsOptionValue @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatTextView(context, attributeSet) {

    init {
        pixelTextSize = displayMinimum() * .05f
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.makeMeasureSpec(determineWidth(), MeasureSpec.EXACTLY)
        super.onMeasure(width, heightMeasureSpec)
    }

    private fun determineWidth(): Int = (displayMinimum() * .2f).toInt()
}
