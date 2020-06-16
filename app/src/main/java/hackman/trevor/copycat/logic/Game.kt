package hackman.trevor.copycat.logic

import java.util.*

/**
 * Summarizes Simon Says and various game modes as:
 *
 * [Game.input] Take a series of int inputs - color buttons pressed mapped to ints
 * [InputResponse] Get success or failed at each step
 * [Game.playBack] Get a series of int outputs - playback next round of colors
 *
 * [canInput] represents game state (playing vs watching)
 */
class Game(private val gameMode: GameMode) {
    private val buttonNumber = 4
    private val random = Random()
    private val sequence: MutableList<Int> = mutableListOf(generateInt())
    private var index = 0

    var canInput = false
        private set

    fun input(pressed: Int): InputResponse =
        if (canInput) canInput(pressed)
        else InputResponse(false)

    fun playBack(): Int? =
        if (!canInput) canPlayBack()
        else null

    private fun generateInt() = random.nextInt(buttonNumber)

    private fun canInput(pressed: Int): InputResponse =
        if (pressed == correctButton()) onSuccess()
        else onFailure()

    private fun onSuccess(): InputResponse {
        if (index == sequence.size - 1) nextLevel()
        else index++
        return InputResponse(true)
    }

    private fun onFailure() = InputResponse(false, correctButton())

    private fun correctButton() = when (gameMode) {
        GameMode.Reverse -> sequence[sequence.size - 1 - index]
        GameMode.Opposite -> buttonNumber - sequence[index]
        else -> sequence[index]
    }

    private fun nextLevel() = when (gameMode) {
        GameMode.Chaos -> {
            for (i in sequence.indices) {
                sequence[i] = generateInt()
            }
            sequence.add(generateInt())
            canInput = false
            index = 0
        }
        else -> {
            sequence.add(generateInt())
            canInput = false
            index = 0
        }
    }

    private fun canPlayBack(): Int = when (gameMode) {
        GameMode.Single -> {
            onFinishedPlayBack()
            sequence[sequence.size - 1]
        }
        else -> {
            val toReturn = sequence[index++]
            if (finishedPlayBack()) onFinishedPlayBack()
            toReturn
        }
    }

    private fun finishedPlayBack() = index >= sequence.size

    private fun onFinishedPlayBack() {
        index = 0
        canInput = true
    }
}

enum class GameMode {
    Classic,
    Reverse,
    Chaos,
    Single,
    Opposite,
    TwoPlayer;
}
