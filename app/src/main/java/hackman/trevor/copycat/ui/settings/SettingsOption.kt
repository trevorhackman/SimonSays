package hackman.trevor.copycat.ui.settings

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import hackman.trevor.copycat.system.displayMinimum
import hackman.trevor.copycat.system.pixelTextSize

class SettingsOption @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatTextView(context, attributeSet) {

    init {
        pixelTextSize = displayMinimum() * .07f
    }
}
