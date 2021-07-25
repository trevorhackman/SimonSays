package hackman.trevor.copycat

import hackman.trevor.copycat.logic.game.*
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
            assert(result is InputSuccessResponse) {
                "Failed playing $sequence Size ${sequence.size}"
            }
        }
        finishInput()
    }

    private fun finishInput() {
        val result = game.finishInput()
        assert(result is InputSuccessResponse)
    }

    @Test
    fun testNormal() {
        game = Game(GameMode.Normal)
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
                GameButton(3 - it.buttonNumber)
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

    @Test
    fun testTwoPlayerVictory() {
        testPlayer1Wins()
        testPlayer2Wins()
        testTwoPlayerTie()
    }

    // Player 2 fails after player 1 succeeds
    private fun testPlayer1Wins() {
        game = Game(GameMode.TwoPlayer)

        var sequence = readSequence()
        sequence.forEach(game::input)
        finishInput()

        sequence = readSequence()
        val wrongSequence = sequence + sequence[0]
        val firstResult = game.input(wrongSequence[0])
        assert(firstResult is InputSuccessResponse)
        val secondResult = game.input(wrongSequence[1])
        assert(secondResult is InputFailedResponse && secondResult.correct == null)

        assert(game.gameOver)
        assert(game.victor == TwoPlayerVictor.Player1)
    }

    // Player 2 succeeds after player 1 fails
    private fun testPlayer2Wins() {
        game = Game(GameMode.TwoPlayer)

        var sequence = readSequence()
        val wrongSequence = sequence + GameButton(0)

        wrongSequence.forEach(game::input)
        finishInput()
        assert(!game.gameOver)

        sequence = readSequence()
        sequence.forEach(game::input)
        finishInput()
        assert(game.gameOver)
        assert(game.victor == TwoPlayerVictor.Player2)
    }

    // PLayer 2 fails after player 1 fails
    private fun testTwoPlayerTie() {
        game = Game(GameMode.TwoPlayer)

        var sequence = readSequence()
        var wrongSequence = sequence + GameButton(0)

        wrongSequence.forEach(game::input)
        finishInput()
        assert(!game.gameOver)

        sequence = readSequence()
        wrongSequence = sequence + GameButton(0)

        wrongSequence.forEach(game::input)
        assert(game.gameOver)
        assert(game.victor == TwoPlayerVictor.Tie)
    }

    @Test
    fun testTimeToFinishInput() {
        testTimeToFinishInputNormal()
        testTimeToFinishInputTwoPlayer()
    }

    private fun testTimeToFinishInputNormal() {
        game = Game(GameMode.Normal)

        assert(!game.isTimeToFinishInput)

        val sequence = readSequence()
        sequence.forEach(game::input)

        assert(game.isTimeToFinishInput)
    }

    private fun testTimeToFinishInputTwoPlayer() {
        game = Game(GameMode.TwoPlayer)

        val sequence = readSequence()
        game.input(GameButton(sequence[0].buttonNumber + 1))

        assert(game.isTimeToFinishInput)
    }
}
