package hackman.trevor.copycat.ui.main.extra_button

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import hackman.trevor.copycat.system.displayMinimum

abstract class ExtraButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatButton(context, attributeSet) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = determineSize()
        setMeasuredDimension(size, size)
    }

    private fun determineSize(): Int = (displayMinimum() * .20).toInt()
}
