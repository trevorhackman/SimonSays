package hackman.trevor.copycat.logic.game

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import hackman.trevor.copycat.R
import hackman.trevor.copycat.logic.settings.Speed
import hackman.trevor.copycat.logic.settings.toSound
import hackman.trevor.copycat.logic.viewmodels.FailureViewModel
import hackman.trevor.copycat.logic.viewmodels.GameViewModel
import hackman.trevor.copycat.observe
import hackman.trevor.copycat.requireValue
import hackman.trevor.copycat.system.SaveData
import hackman.trevor.copycat.system.StringResource
import hackman.trevor.copycat.system.report
import hackman.trevor.copycat.system.sound.SoundManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GamePlayer(
    private val gameViewModel: GameViewModel,
    private val failureViewModel: FailureViewModel,
    private val lifecycle: Lifecycle
) : LifecycleOwner {

    private lateinit var game: Game
    private lateinit var sequence: List<GameButton>
    private lateinit var speed: Speed

    private lateinit var playerJob: Job

    // For better user experience, don't fail until all buttons are released
    private var waitingToFail = false

    private var lastPressed: GameButton? = null
    private var correctButton: GameButton? = null

    private val score: Int
        get() = gameViewModel.roundNumber.requireValue().roundNumber - 1

    init {
        observeGameState()
        observePlayerPushed()
    }

    fun startGame() {
        waitingToFail = false
        gameViewModel.roundNumber.value = RoundNumber.start
        game = Game(gameViewModel.gameMode.requireValue())
        speed = SaveData.speed
        updateTwoPlayerInfoText()
    }

    private fun observeGameState() = observe(gameViewModel.gameState) {
        if (it == GameState.Watch) playSequence()
        else if (!it.inGame) stopPlayBack()
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
        if (score == 0) delay(startGameDelay)
        else if (gameViewModel.gameMode.requireValue() == GameMode.TwoPlayer) delay(twoPlayerTurnSwitchDelay)
        delay(speed.startDelay)
        sequence.forEachIndexed { i, gameButton ->
            playButton(gameButton, i == sequence.lastIndex)
        }
        gameViewModel.gameState.value = GameState.Input
    }

    private suspend fun playButton(button: GameButton, isLast: Boolean) {
        gameViewModel.buttonPlayBack.value = button
        delay(speed.lightDuration)
        gameViewModel.buttonPlayBack.value = null
        if (!isLast) delay(speed.delayDuration)
    }

    private fun stopPlayBack() = playerJob.cancel().also {
        gameViewModel.buttonPlayBack.value = null
    }

    private fun observePlayerPushed() = observe(gameViewModel.playerPushed) {
        if (gameViewModel.gameState.value != GameState.Input) return@observe
        when (it) {
            null -> onAllReleased()
            else -> onPress(it)
        }
    }

    private fun onAllReleased() {
        if (waitingToFail) onFailure()
        if (game.isTimeToFinishInput) {
            val result = game.finishInput()
            // TODO Should never happen, check crashlytics and remove this todo once I have some data
            if (result is InputFailedResponse) report("GamePlayer made a faulty call to finish input")

            // Happens when player 2 succeeds after player 1 fails
            if (game.gameOver) onGameOver()
            else {
                gameViewModel.gameState.value = GameState.Watch
                gameViewModel.roundNumber.value = game.roundNumber
                updateHighScore()
                updateTwoPlayerInfoText()
            }
        }
    }

    private fun updateHighScore() {
        if (isNewHighScore()) setNewHighScore()
    }

    private fun isNewHighScore(): Boolean {
        val gameMode = gameViewModel.gameMode.requireValue()
        val oldHighScore = SaveData.getHighScore(gameMode)
        return score > oldHighScore
    }

    private fun setNewHighScore() = SaveData.saveHighScore(
        gameViewModel.gameMode.requireValue(),
        gameViewModel.roundNumber.requireValue().roundNumber - 1
    )

    private fun updateTwoPlayerInfoText() {
        if (gameViewModel.gameMode.requireValue() == GameMode.TwoPlayer) {
            gameViewModel.infoText.value = when (game.isSecondPlayerTurn) {
                false -> StringResource(R.string.info_text_player1)
                true -> StringResource(R.string.info_text_player2)
            }
        }
    }

    private fun onFailure() {
        SoundManager.stopAllSounds() // Stop other sounds so failure sound isn't hidden
        SaveData.failureSound.toSound().play()
        failureViewModel.apply {
            pressed.value = lastPressed
            correct.value = correctButton
        }
        if (game.gameOver) onGameOver()
        else waitingToFail = false
    }

    private fun onGameOver() {
        failureViewModel.apply {
            if (gameViewModel.gameMode.requireValue() == GameMode.TwoPlayer) setTwoPlayerVictor(game.victor)
            else setMode(gameViewModel.gameMode.requireValue())
            score.value = Score(this@GamePlayer.score)
            best.value = Score.getHighScore(gameViewModel.gameMode.requireValue())
        }
        gameViewModel.gameState.value = GameState.Failure
    }

    private fun onPress(gameButton: GameButton) {
        lastPressed = gameButton
        handleResponse(game.input(gameButton))
    }

    private fun handleResponse(response: InputResponse) {
        if (response is InputFailedResponse) {
            waitingToFail = true
            correctButton = response.correct
        }
    }

    override fun getLifecycle() = lifecycle

    companion object {
        private const val startGameDelay = 1000L
        private const val twoPlayerTurnSwitchDelay = 500L
    }
}
