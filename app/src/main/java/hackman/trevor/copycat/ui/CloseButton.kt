package hackman.trevor.copycat.ui

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.setPadding
import hackman.trevor.copycat.R
import hackman.trevor.copycat.system.displayMinimum
import hackman.trevor.copycat.system.getDrawable
import hackman.trevor.copycat.system.getString
import hackman.trevor.copycat.system.pixelTextSize

class CloseButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatButton(context, attributeSet) {
    init {
        background = getDrawable(R.drawable.bordered_rectangle_light)
        typeface = Typeface.DEFAULT_BOLD
        pixelTextSize = displayMinimum() * .055f
        setPadding(0)
        text = getString(R.string.close)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = MeasureSpec.makeMeasureSpec(determineHeight(), MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, height)
    }

    private fun determineHeight() = (displayMinimum() * .11).toInt()
}
