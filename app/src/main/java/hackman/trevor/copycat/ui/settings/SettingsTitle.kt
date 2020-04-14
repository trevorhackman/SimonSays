package hackman.trevor.copycat.ui.settings

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView
import hackman.trevor.copycat.R
import hackman.trevor.copycat.system.displayMinimum
import hackman.trevor.copycat.system.getString

class SettingsTitle @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatTextView(context, attributeSet) {

    init {
        text = getString(R.string.settings_title)
        setTextSize(TypedValue.COMPLEX_UNIT_PX, displayMinimum() * .15f)
    }
}