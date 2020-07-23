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

    private lateinit var game: Game
    private lateinit var sequence: List<GameButton>

    /** For better user experience, don't fail until all buttons are released */
    private var waitingToFail = false

    init {
        observeGameState()
        observePlayerPushed()
    }

    fun startGame() {
        waitingToFail = false
        game = Game(gameViewModel.gameMode.requireValue())
        gameViewModel.setRoundNumber(RoundNumber(1))
    }

    private fun observeGameState() = observe(gameViewModel.gameState) {
        if (it == GameState.Watch) {
            playSequence()
        }
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
        delay(500)
    }

    private fun observePlayerPushed() = observe(gameViewModel.playerPushed) {
        if (gameViewModel.gameState.value != GameState.Input) return@observe
        when (it) {
            null -> onAllReleased()
            else -> onPress(game.input(it))
        }
    }

    private fun onAllReleased() {
        if (waitingToFail) gameViewModel.setGameState(GameState.MainMenu)
        else if (!game.canInput) {
            gameViewModel.setGameState(GameState.Watch)
            gameViewModel.setRoundNumber(RoundNumber(game.sequenceSize))
        }
    }

    private fun onPress(response: InputResponse) {
        if (!response.isSuccess) waitToFail()
    }

    private fun waitToFail() {
        waitingToFail = true
    }

    override fun getLifecycle(): Lifecycle = lifecycle
}
