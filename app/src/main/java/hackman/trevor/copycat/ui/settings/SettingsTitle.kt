package hackman.trevor.copycat.ui.settings

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import hackman.trevor.copycat.R
import hackman.trevor.copycat.system.displayMinimum
import hackman.trevor.copycat.system.getString
import hackman.trevor.copycat.system.pixelTextSize

class SettingsTitle @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatTextView(context, attributeSet) {

    init {
        text = getString(R.string.settings_title)
        pixelTextSize = displayMinimum() * .11f
    }
}
