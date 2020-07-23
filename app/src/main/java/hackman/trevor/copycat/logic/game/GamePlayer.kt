package hackman.trevor.copycat.logic.game

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import hackman.trevor.copycat.logic.viewmodels.GameViewModel
import hackman.trevor.copycat.observe
import hackman.trevor.copycat.requireValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GamePlayer(
    private val gameViewModel: GameViewModel,
    private val lifecycle: Lifecycle
) : LifecycleOwner {

    private val gameMode = gameViewModel.gameMode.requireValue()
    private val game = Game(gameMode)

    private lateinit var sequence: List<GameButton>

    init {
        observeGameState()
    }

    private fun observeGameState() = observe(gameViewModel.gameState) {
        if (it == GameState.Watch) playSequence()
    }

    private fun playSequence() {
        readSequence()
        playOneAtATime()
    }

    private fun readSequence() {
        sequence = mutableListOf<GameButton>().apply {
            while (!game.canInput) {
                add(game.playBack()!!)
            }
        }
    }

    private fun playOneAtATime() {
        lifecycleScope.launch {
            sequence.forEach { playButton(it) }
            gameViewModel.setGameState(GameState.Input)
        }
    }

    private suspend fun playButton(button: GameButton) {
        gameViewModel.setButtonPlayBack(button)
        delay(1000)
        gameViewModel.setButtonPlayBack(null)
    }

    override fun getLifecycle(): Lifecycle = lifecycle
}
