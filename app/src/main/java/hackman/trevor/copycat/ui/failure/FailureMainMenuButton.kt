package hackman.trevor.copycat.ui.failure

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import hackman.trevor.copycat.R
import hackman.trevor.copycat.logic.game.GameState
import hackman.trevor.copycat.logic.viewmodels.GameViewModel
import hackman.trevor.copycat.system.displayMinimum
import hackman.trevor.copycat.system.getDrawable
import hackman.trevor.copycat.system.getString
import hackman.trevor.copycat.system.pixelTextSize

class FailureMainMenuButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatButton(context, attributeSet) {

    private lateinit var gameViewModel: GameViewModel

    init {
        background = getDrawable(R.drawable.bordered_rectangle_light)
        text = getString(R.string.failure_main_menu)
        typeface = Typeface.DEFAULT_BOLD
        pixelTextSize = displayMinimum() * .055f
    }

    fun setup(gameViewModel: GameViewModel) {
        this.gameViewModel = gameViewModel
        setOnClickListener()
    }

    fun setOnClickListener() = setOnClickListener {
        gameViewModel.setGameState(GameState.MainMenu)
    }
}
