package hackman.trevor.copycat.ui.game_modes

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView
import hackman.trevor.copycat.R
import hackman.trevor.copycat.logic.GameMode
import hackman.trevor.copycat.system.SaveData
import hackman.trevor.copycat.system.displayMinimum
import hackman.trevor.copycat.system.getString

class GameModesBestText @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatTextView(context, attributeSet) {

    init {
        updateText(SaveData(context).gameMode)
        setTextSize(TypedValue.COMPLEX_UNIT_PX, displayMinimum() * .04f)
    }

    fun updateText(gameMode: GameMode) {
        val gameModeName = getString(gameMode.name())
        val gameModeHighScore = SaveData(context).getHighScore(gameMode)
        text = getString(R.string.game_modes_best_text_format, gameModeName, gameModeHighScore)
    }
}
