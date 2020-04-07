package hackman.trevor.copycat.ui.main.extra_button

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import hackman.trevor.copycat.system.displayMinimum
import hackman.trevor.copycat.ui.CircularTouchListener

/**
 * Parent class for main-screen side buttons
 * We are overriding performClick but lint doesn't realize it
 */
@SuppressLint("ClickableViewAccessibility")
abstract class ExtraButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatButton(context, attributeSet) {

    init {
        isClickable = false
        setOnTouchListener(CircularTouchListener)
    }

    final override fun setOnTouchListener(listener: OnTouchListener?) =
        super.setOnTouchListener(listener)

    final override fun performClick(): Boolean {
        super.performClick()
        click()
        return true
    }

    abstract fun click()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = determineSize()
        setMeasuredDimension(size, size)
    }

    private fun determineSize(): Int = (displayMinimum() * .20).toInt()
}
