package hackman.trevor.copycat.ui.failure

import android.content.Context
import android.util.AttributeSet
import hackman.trevor.copycat.R
import hackman.trevor.copycat.logic.game.GameState
import hackman.trevor.copycat.logic.viewmodels.GameViewModel
import hackman.trevor.copycat.system.getString

class FailurePlayAgainButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : FailureButton(context, attributeSet) {

    private lateinit var gameViewModel: GameViewModel

    init {
        text = getString(R.string.failure_play_again)
    }

    fun setup(gameViewModel: GameViewModel) {
        this.gameViewModel = gameViewModel
        setOnClickListener()
    }

    fun setOnClickListener() = setOnClickListener {
        gameViewModel.gameState.value = GameState.Watch
    }
}
