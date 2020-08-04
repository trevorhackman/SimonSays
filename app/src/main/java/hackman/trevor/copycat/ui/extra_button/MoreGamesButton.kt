package hackman.trevor.copycat.ui.extra_button

import android.content.Context
import android.util.AttributeSet
import hackman.trevor.copycat.R
import hackman.trevor.copycat.system.ShowMoreGames
import hackman.trevor.copycat.system.getDrawable
import hackman.trevor.copycat.system.sound.SoundManager
import hackman.trevor.copycat.ui.DialogFactory
import hackman.trevor.copycat.ui.showCorrectly

class MoreGamesButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : ExtraButton(context, attributeSet) {

    private val moreGamesDialog by lazy {
        DialogFactory.viewMoreGames {
            ShowMoreGames.startMoreGamesIntent(context)
        }
    }

    init {
        background = getDrawable(R.drawable.more_games)
        setOnClickListener {
            SoundManager.click.play()
            moreGamesDialog.showCorrectly()
        }
    }
}
