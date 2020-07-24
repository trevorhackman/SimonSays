package hackman.trevor.copycat.logic.game

import java.util.*

/**
 * Summarizes Simon Says and various game modes as:
 *
 * [Game.input] Take a series of int inputs - color buttons pressed mapped to ints
 * [InputResponse] Get success or failed at each step
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

    val roundNumber
        get() = when (gameMode) {
            GameMode.TwoPlayer -> twoPlayerData.secondSequence.size
            else -> sequence.size
        }

    var canInput = false
        private set

    fun input(pressed: GameButton): InputResponse =
        if (canInput) processInput(pressed)
        else InputResponse(false)

    fun playBack(): GameButton? =
        if (!canInput) processPlayBack()
        else null

    private fun generateGameButton() = GameButton(random.nextInt(buttonNumber))

    private fun processInput(pressed: GameButton): InputResponse =
        if (pressed == correctButton()) onSuccess()
        else onFailure()

    private fun onSuccess(): InputResponse {
        if (index == selectSequence.size - 1) nextLevel()
        else index++
        return InputResponse(true)
    }

    private fun onFailure() = InputResponse(false, correctButton())

    private fun correctButton() = when (gameMode) {
        GameMode.Reverse -> sequence[sequence.size - 1 - index]
        GameMode.Opposite -> GameButton(buttonNumber - sequence[index].buttonNumber - 1)
        else -> selectSequence[index]
    }

    private fun nextLevel() {
        if (gameMode == GameMode.Chaos) {
            for (i in sequence.indices) {
                sequence[i] = generateGameButton()
            }
        }

        selectSequence.add(generateGameButton())
        canInput = false
        index = 0

        if (gameMode == GameMode.TwoPlayer) {
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
}
