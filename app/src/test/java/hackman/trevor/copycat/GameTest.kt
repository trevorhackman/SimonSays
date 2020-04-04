package hackman.trevor.copycat

import hackman.trevor.copycat.logic.Game
import hackman.trevor.copycat.logic.GameMode
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Test playing each game mode for two rounds
 */
class GameTest {
    private lateinit var game: Game
    private lateinit var sequence: MutableList<Int>

    @Test
    fun testClassic() {
        game = Game(GameMode.Classic)
        assertEquals(false, game.canInput)
        sequence = mutableListOf()
        while (!game.canInput) {
            sequence.add(game.playBack() ?: throw Exception())
        }
        assertEquals(1, sequence.size)
        sequence.forEach {
            val response = game.input(it)
            assertEquals(true, response.isSuccess)
        }
        assertEquals(false, game.canInput)
        sequence = mutableListOf()
        while (!game.canInput) {
            sequence.add(game.playBack() ?: throw Exception())
        }
        assertEquals(2, sequence.size)
        sequence.forEach {
            val response = game.input(it)
            assertEquals(true, response.isSuccess)
        }
        assertEquals(false, game.canInput)
    }

    @Test
    fun testReverse() {
        game = Game(GameMode.Reverse)
        assertEquals(false, game.canInput)
        sequence = mutableListOf()
        while (!game.canInput) {
            sequence.add(game.playBack() ?: throw Exception())
        }
        assertEquals(1, sequence.size)
        for (i in sequence.indices) {
            val response = game.input(sequence.last() - i)
            assertEquals(true, response.isSuccess)
        }
        assertEquals(false, game.canInput)
        sequence = mutableListOf()
        while (!game.canInput) {
            sequence.add(game.playBack() ?: throw Exception())
        }
        assertEquals(2, sequence.size)
        for (it in sequence.indices) {
            val response = game.input(sequence[sequence.size - 1 - it])
            assertEquals(true, response.isSuccess)
        }
        assertEquals(false, game.canInput)
    }

    @Test
    fun testChaos() {
        game = Game(GameMode.Chaos)
        assertEquals(false, game.canInput)
        sequence = mutableListOf()
        while (!game.canInput) {
            sequence.add(game.playBack() ?: throw Exception())
        }
        assertEquals(1, sequence.size)
        sequence.forEach {
            val response = game.input(it)
            assertEquals(true, response.isSuccess)
        }
        sequence = mutableListOf()
        while (!game.canInput) {
            sequence.add(game.playBack() ?: throw Exception())
        }
        assertEquals(2, sequence.size)
        sequence.forEach {
            val response = game.input(it)
            assertEquals(true, response.isSuccess)
        }
        assertEquals(false, game.canInput)
    }

    @Test
    fun testSingle() {
        game = Game(GameMode.Single)
        assertEquals(false, game.canInput)
        sequence = mutableListOf()
        while (!game.canInput) {
            sequence.add(game.playBack() ?: throw Exception())
        }
        assertEquals(1, sequence.size)
        sequence.forEach {
            val response = game.input(it)
            assertEquals(true, response.isSuccess)
        }
        sequence.add(game.playBack() ?: throw Exception())
        assertEquals(2, sequence.size)
        sequence.forEach {
            val response = game.input(it)
            assertEquals(true, response.isSuccess)
        }
        assertEquals(false, game.canInput)
    }

    @Test
    fun testOpposite() {
        game = Game(GameMode.Opposite)
        assertEquals(false, game.canInput)
        sequence = mutableListOf()
        while (!game.canInput) {
            sequence.add(game.playBack() ?: throw Exception())
        }
        assertEquals(1, sequence.size)
        sequence.forEach {
            val response = game.input(4 - it)
            assertEquals(true, response.isSuccess)
        }
        assertEquals(false, game.canInput)
        sequence = mutableListOf()
        while (!game.canInput) {
            sequence.add(game.playBack() ?: throw Exception())
        }
        assertEquals(2, sequence.size)
        sequence.forEach {
            val response = game.input(4 - it)
            assertEquals(true, response.isSuccess)
        }
        assertEquals(false, game.canInput)
    }
}
