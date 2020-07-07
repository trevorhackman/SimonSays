package hackman.trevor.copycat.ui.main.main_button

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import hackman.trevor.copycat.system.displayMinimum
import hackman.trevor.copycat.system.pixelTextSize

class MainButtonGameModeText @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatTextView(context, attributeSet) {

    init {
        pixelTextSize = displayMinimum() * .04f
        isAllCaps = true
    }
}
