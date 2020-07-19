package hackman.trevor.copycat

import hackman.trevor.copycat.logic.game.Game
import hackman.trevor.copycat.logic.game.GameButton
import hackman.trevor.copycat.logic.game.GameMode
import org.junit.Test

/**
 * Test playing 100 rounds of each game mode
 */
class GameTest {
    private lateinit var game: Game

    // Imitate player watching playback
    private fun readSequence(): List<GameButton> {
        return mutableListOf<GameButton>().apply {
            while (!game.canInput) {
                add(game.playBack()!!)
            }
        }
    }

    // Imitate player input
    private fun playSequence(sequence: List<GameButton>) {
        sequence.forEach {
            val result = game.input(it)
            assert(result.isSuccess) {
                "Failed playing $sequence Size ${sequence.size}"
            }
        }
    }

    @Test
    fun testClassic() {
        game = Game(GameMode.Classic)
        var previousSequence = listOf<GameButton>()
        repeat(100) {
            val sequence = readSequence()
            playSequence(sequence)

            assert(sequence.subList(0, sequence.size - 1) == previousSequence)
            previousSequence = sequence
        }
    }

    @Test
    fun testReverse() {
        game = Game(GameMode.Reverse)
        repeat(100) {
            val sequence = readSequence()
            val reversed = sequence.reversed()
            playSequence(reversed)
        }
    }

    @Test
    fun testChaos() {
        game = Game(GameMode.Chaos)
        repeat(100) {
            val sequence = readSequence()
            playSequence(sequence)
        }
    }

    @Test
    fun testSingle() {
        game = Game(GameMode.Single)

        val cumulativeSequence = mutableListOf<GameButton>()
        repeat(100) {
            val sequence = readSequence()
            cumulativeSequence.add(sequence.first())
            playSequence(cumulativeSequence)

            assert(sequence.size == 1)
        }
    }

    @Test
    fun testOpposite() {
        game = Game(GameMode.Opposite)
        repeat(100) {
            val sequence = readSequence()
            val inverted = sequence.map {
                GameButton(4 - it.buttonNumber)
            }
            playSequence(inverted)
        }
    }

    @Test
    fun testTwoPlayer() {
        game = Game(GameMode.TwoPlayer)
        var player1Sequence = listOf<GameButton>()
        var player2Sequence = listOf<GameButton>()
        repeat(200) { count ->
            val sequence = readSequence()
            playSequence(sequence)

            assert(sequence.size == count / 2 + 1)
            if (count % 2 == 0) {
                assert(sequence.subList(0, sequence.size - 1) == player1Sequence)
                player1Sequence = sequence
            } else {
                assert(sequence.subList(0, sequence.size - 1) == player2Sequence)
                player2Sequence = sequence
            }
        }
    }
}
