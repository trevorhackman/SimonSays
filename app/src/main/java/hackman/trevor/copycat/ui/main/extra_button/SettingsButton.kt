package hackman.trevor.copycat.ui.main.extra_button

import android.content.Context
import android.util.AttributeSet
import hackman.trevor.copycat.R
import hackman.trevor.copycat.system.getDrawable

class SettingsButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : ExtraButton(context, attributeSet) {

    init {
        background = getDrawable(R.drawable.gear)
    }
}
