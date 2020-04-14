package hackman.trevor.copycat.ui.settings

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView
import hackman.trevor.copycat.system.displayMinimum

class SettingsOption @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatTextView(context, attributeSet) {

    init {
        setTextSize(TypedValue.COMPLEX_UNIT_PX, displayMinimum() * .05f)
    }
}