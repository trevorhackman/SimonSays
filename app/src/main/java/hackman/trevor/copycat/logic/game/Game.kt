package hackman.trevor.copycat.logic.game

import java.util.*

/**
 * Summarizes Simon Says and various game modes as:
 *
 * [Game.input] Take a series of int inputs - color buttons pressed mapped to ints
 * [InputResponse] Get success or failed at each step
 * [Game.finishInput] 'Enter', when done inputting, move onto next round if successful
 * [Game.playBack] Get a series of int outputs - playback next round of colors
 *
 * [processInput] represents game state (playing vs watching)
 */
class Game(private val gameMode: GameMode) {
    private val buttonNumber = 4
    private val random = Random()
    private var index = 0

    private val sequence = mutableListOf(generateGameButton())
    private val twoPlayerData = TwoPlayerData(generateGameButton())

    private val selectSequence
        get() = when (gameMode) {
            GameMode.TwoPlayer ->
                if (twoPlayerData.isSecondPlayerTurn) twoPlayerData.secondSequence
                else sequence
            else -> sequence
        }

    val isTimeToFinishInput
        get() = !gameOver && (index == -1 ||
                (gameMode == GameMode.TwoPlayer && !twoPlayerData.isSecondPlayerTurn && twoPlayerData.player1Failed))

    val roundNumber
        get() = when (gameMode) {
            GameMode.TwoPlayer -> RoundNumber(twoPlayerData.secondSequence.size)
            else -> RoundNumber(sequence.size)
        }

    var canInput = false
        private set

    // Indefinitely true once game is over
    var gameOver = false
        private set

    val victor
        get() = twoPlayerData.victor

    fun input(pressed: GameButton): InputResponse =
        if (canInput) processInput(pressed)
        else onBadInputFailure()

    fun playBack(): GameButton? =
        if (!canInput) processPlayBack()
        else null

    fun finishInput(): InputResponse =
        if (isTimeToFinishInput) InputSuccessResponse.also { onNextRound() }
        else onBadInputFailure()

    private fun generateGameButton() = GameButton(random.nextInt(buttonNumber))

    private fun processInput(pressed: GameButton): InputResponse =
        if (pressed == correctButton()) onSuccess()
        else onWrongButtonFailure()

    private fun onSuccess() = InputSuccessResponse.also {
        if (index == selectSequence.size - 1) {
            canInput = false
            index = -1
        } else index++
    }

    private fun onBadInputFailure() = InputFailedResponse(null).also {
        onFailure()
    }

    private fun onWrongButtonFailure() = InputFailedResponse(correctButton()).also {
        onFailure()
    }

    private fun onFailure() = when (gameMode) {
        GameMode.TwoPlayer -> onTwoPlayerFailure()
        else -> gameOver = true
    }

    private fun onTwoPlayerFailure() {
        if (twoPlayerData.isSecondPlayerTurn) onSecondPlayerFailure()
        else onFirstPlayerFailure()
    }

    private fun onSecondPlayerFailure() {
        gameOver = true
        determineTieOrPlayer1Victory()
    }

    private fun determineTieOrPlayer1Victory() {
        twoPlayerData.victor = if (twoPlayerData.player1Failed) {
            TwoPlayerVictor.Tie
        } else {
            TwoPlayerVictor.Player1
        }
    }

    private fun onFirstPlayerFailure() {
        twoPlayerData.player1Failed = true
    }

    private fun correctButton() = when (gameMode) {
        GameMode.Reverse -> sequence[sequence.size - 1 - index]
        GameMode.Opposite -> GameButton(buttonNumber - sequence[index].buttonNumber - 1)
        else -> selectSequence[index]
    }

    private fun onNextRound() {
        if (gameMode == GameMode.Chaos) {
            for (i in sequence.indices) {
                sequence[i] = generateGameButton()
            }
        }

        selectSequence.add(generateGameButton())
        canInput = false
        index = 0

        if (gameMode == GameMode.TwoPlayer) {
            if (twoPlayerData.isSecondPlayerTurn && twoPlayerData.player1Failed) {
                gameOver = true
                twoPlayerData.victor = TwoPlayerVictor.Player2
            }
            twoPlayerData.isSecondPlayerTurn = !twoPlayerData.isSecondPlayerTurn
        }
    }

    private fun processPlayBack() = when (gameMode) {
        GameMode.Single -> {
            onFinishedPlayBack()
            sequence[sequence.size - 1]
        }
        else -> {
            val toReturn = selectSequence[index++]
            if (finishedPlayBack()) onFinishedPlayBack()
            toReturn
        }
    }

    private fun finishedPlayBack() = index >= selectSequence.size

    private fun onFinishedPlayBack() {
        index = 0
        canInput = true
    }
}

private class TwoPlayerData(randomButton: GameButton) {
    val secondSequence = mutableListOf(randomButton)
    var isSecondPlayerTurn = false

    // When player 1 fails, we play one more sequence for player 2, giving player 1 a chance to tie
    var player1Failed = false

    lateinit var victor: TwoPlayerVictor
}

enum class TwoPlayerVictor { Tie, Player1, Player2 }
