package hackman.trevor.copycat

import junit.framework.Assert.assertEquals
import org.junit.Test

/*

 */

class MaxProfitTest {
    @Test
    fun test() {
        assertEquals(0, MaxProfit.findMaxProfit(listOf()))
        assertEquals(0, MaxProfit.findMaxProfit(listOf(10)))
        assertEquals(0, MaxProfit.findMaxProfit(listOf(10, 5, 2, 1)))
        assertEquals(9, MaxProfit.findMaxProfit(listOf(1, 2, 5, 10)))
        assertEquals(6, MaxProfit.findMaxProfit(listOf(10, 7, 5, 11)))
        assertEquals(8, MaxProfit.findMaxProfit(listOf(10, 7, 5, 11, 2, 10)))
    }
}

class MaxProfit private constructor(private val sequence: List<Int>) {
    companion object {
        fun findMaxProfit(sequence: List<Int>): Int =
            if (tooSmall(sequence)) 0
            else MaxProfit(sequence).findMaxProfit()

        private fun tooSmall(sequence: List<Int>) = sequence.size < 2
    }

    private var maxProfit = 0
    private var minPrice = sequence[0]
    private var nextPrice = sequence[1]

    fun findMaxProfit(): Int {
        for (i in 1 until sequence.size) {
            nextPrice = sequence[i]
            nextNumber()
        }

        return maxProfit
    }

    private fun nextNumber() {
        if (nextPrice - minPrice > maxProfit) newBest()
        else if (nextPrice < minPrice) minPrice = nextPrice
    }

    private fun newBest() {
        maxProfit = nextPrice - minPrice
    }
}
