package hackman.trevor.copycat.ui.game_modes

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import hackman.trevor.copycat.R
import hackman.trevor.copycat.logic.game.GameMode
import hackman.trevor.copycat.system.SaveData
import hackman.trevor.copycat.system.displayMinimum
import hackman.trevor.copycat.system.getString
import hackman.trevor.copycat.system.pixelTextSize

class GameModesBestText @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatTextView(context, attributeSet) {

    init {
        pixelTextSize = displayMinimum() * .04f
        updateText(SaveData.gameMode)
    }

    fun updateText(gameMode: GameMode) {
        val gameModeName = getString(gameMode.name())
        val gameModeHighScore = SaveData.getHighScore(gameMode)
        text = getString(R.string.game_modes_best_text_format, gameModeName, gameModeHighScore)
    }
}
