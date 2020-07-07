package hackman.trevor.copycat.ui.game_modes

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import hackman.trevor.copycat.R
import hackman.trevor.copycat.system.displayMinimum
import hackman.trevor.copycat.system.getString
import hackman.trevor.copycat.system.pixelTextSize

class GameModesTitle @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatTextView(context, attributeSet) {

    init {
        text = getString(R.string.game_modes_title)
        pixelTextSize = displayMinimum() * .11f
    }
}
