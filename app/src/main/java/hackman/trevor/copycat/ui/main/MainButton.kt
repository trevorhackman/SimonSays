package hackman.trevor.copycat.ui.main

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatButton
import hackman.trevor.copycat.R
import hackman.trevor.copycat.system.Color
import hackman.trevor.copycat.system.displayMinimum
import hackman.trevor.copycat.system.getDrawable
import hackman.trevor.copycat.ui.CircularTouchListener

class MainButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatButton(context, attributeSet) {

    init {
        background = getDrawable(R.drawable.circle)
        setTypeface(null, Typeface.BOLD)
        setTextColor(Color.White)
        isClickable = false
        setOnTouchListener(CircularTouchListener)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = determineSize()
        setMeasuredDimension(size, size)
        setTextSize(TypedValue.COMPLEX_UNIT_PX, determineTextSize(size))
    }

    private fun determineSize(): Int = (displayMinimum() * .40).toInt()

    private fun determineTextSize(buttonSize: Int): Float = buttonSize * .40f

    override fun performClick(): Boolean {
        super.performClick()

        // TODO ACTION

        return true
    }
}
