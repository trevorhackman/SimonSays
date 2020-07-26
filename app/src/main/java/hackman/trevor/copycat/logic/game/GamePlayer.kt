package hackman.trevor.copycat.logic.game

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import hackman.trevor.copycat.logic.settings.Speed
import hackman.trevor.copycat.logic.viewmodels.GameViewModel
import hackman.trevor.copycat.observe
import hackman.trevor.copycat.requireValue
import hackman.trevor.copycat.system.SaveData
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GamePlayer(
    private val gameViewModel: GameViewModel,
    private val lifecycle: Lifecycle
) : LifecycleOwner {

    private lateinit var game: Game
    private lateinit var sequence: List<GameButton>
    private lateinit var speed: Speed

    private lateinit var playerJob: Job

    // For better user experience, don't fail until all buttons are released
    private var waitingToFail = false

    init {
        observeGameState()
        observePlayerPushed()
    }

    fun startGame() {
        waitingToFail = false
        gameViewModel.setRoundNumber(RoundNumber(1))
        game = Game(gameViewModel.gameMode.requireValue())
        speed = SaveData.speed
    }

    private fun observeGameState() = observe(gameViewModel.gameState) {
        if (it == GameState.Watch) playSequence()
        else if (it != GameState.Input) stopPlayBack()
    }

    private fun playSequence() {
        readSequence()
        playerJob = playOneAtATime()
    }

    private fun readSequence() {
        sequence = mutableListOf<GameButton>().apply {
            while (!game.canInput) {
                add(game.playBack()!!)
            }
        }
    }

    private fun playOneAtATime() = lifecycleScope.launch {
        delay(speed.startDelay)
        sequence.forEachIndexed { i, gameButton ->
            playButton(gameButton, i == sequence.lastIndex)
        }
        gameViewModel.setGameState(GameState.Input)
    }

    private suspend fun playButton(button: GameButton, isLast: Boolean) {
        gameViewModel.setButtonPlayBack(button)
        delay(speed.lightDuration)
        gameViewModel.setButtonPlayBack(null)
        if (!isLast) delay(speed.delayDuration)
    }

    private fun stopPlayBack() = playerJob.cancel()

    private fun observePlayerPushed() = observe(gameViewModel.playerPushed) {
        if (gameViewModel.gameState.value != GameState.Input) return@observe
        when (it) {
            null -> onAllReleased()
            else -> onPress(game.input(it))
        }
    }

    private fun onAllReleased() {
        if (waitingToFail) gameViewModel.setGameState(GameState.Failure)
        else if (!game.canInput) {
            gameViewModel.setGameState(GameState.Watch)
            gameViewModel.setRoundNumber(RoundNumber(game.roundNumber))
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
