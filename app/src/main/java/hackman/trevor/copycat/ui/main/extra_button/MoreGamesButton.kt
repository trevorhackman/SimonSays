package hackman.trevor.copycat.ui.main.extra_button

import android.content.Context
import android.util.AttributeSet
import hackman.trevor.copycat.R
import hackman.trevor.copycat.system.ShowMoreGames
import hackman.trevor.copycat.system.getDrawable
import hackman.trevor.copycat.system.sound.SoundManager
import hackman.trevor.copycat.ui.DialogFactory

class MoreGamesButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : ExtraButton(context, attributeSet) {

    private lateinit var soundManager: SoundManager

    private val moreGamesDialog by lazy {
        DialogFactory(context).viewMoreGames {
            ShowMoreGames.startMoreGamesIntent(context)
        }
    }

    init {
        background = getDrawable(R.drawable.more_games)
        setOnClickListener {
            soundManager.click.play()
            moreGamesDialog.show()
        }
    }

    fun setup(soundManager: SoundManager) {
        this.soundManager = soundManager
    }
}
