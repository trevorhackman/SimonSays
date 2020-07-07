package hackman.trevor.copycat.ui.main

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import hackman.trevor.copycat.R
import hackman.trevor.copycat.system.displayMinimum
import hackman.trevor.copycat.system.dpToPixel
import hackman.trevor.copycat.system.getDrawable
import hackman.trevor.copycat.system.pixelTextSize

class Instructions @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatTextView(context, attributeSet) {

    init {
        background = getDrawable(R.drawable.instructions_rectangle)
        pixelTextSize = displayMinimum() * .054f
        setPadding(dpToPixel(8))
        isVisible = false
        alpha = 0f
    }
}
