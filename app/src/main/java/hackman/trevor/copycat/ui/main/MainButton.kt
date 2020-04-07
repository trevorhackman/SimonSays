package hackman.trevor.copycat.ui.main

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.appcompat.widget.AppCompatButton
import hackman.trevor.copycat.R
import hackman.trevor.copycat.system.Color
import hackman.trevor.copycat.system.displayMinimum

class MainButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatButton(context, attributeSet) {

    init {
        background = context.getDrawable(R.drawable.circle)
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

/**
 * Gives circular touch listener
 * Assumes view is square
 */
private object CircularTouchListener : OnTouchListener {
    /**
     * Returning true causes the view to consume the event
     * Returning false causes the view to pass the event, allowing others to potentially consume
     */
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        val xFromTopLeft = event.x.toDouble()
        val yFromTopLeft = event.y.toDouble()

        val xFromCenter = xFromTopLeft - view.width / 2.0
        val yFromCenter = yFromTopLeft - view.height / 2.0

        val radius = view.width / 2

        if (xFromCenter * xFromCenter + yFromCenter * yFromCenter <= radius * radius) {
            if (event.action == MotionEvent.ACTION_UP) {
                view.performClick()
            }
            return true
        }
        return false
    }
}
