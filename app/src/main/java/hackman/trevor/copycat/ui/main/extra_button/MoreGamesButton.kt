package hackman.trevor.copycat.ui.main.extra_button

import android.content.Context
import android.util.AttributeSet
import hackman.trevor.copycat.R
import hackman.trevor.copycat.system.Color
import hackman.trevor.copycat.system.createRippleDrawable
import hackman.trevor.copycat.system.getDrawable

class MoreGamesButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : ExtraButton(context, attributeSet) {

    init {
        background = createRippleDrawable(getDrawable(R.drawable.more_games_base), Color.White)
    }

    override fun click() {

    }
}
