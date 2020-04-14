package hackman.trevor.copycat.ui.settings

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import hackman.trevor.copycat.system.displayMinimum

class SettingsArrowButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatButton(context, attributeSet) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = determineSize()
        setMeasuredDimension(size, size)
    }

    private fun determineSize(): Int = (displayMinimum() * .12).toInt()

}