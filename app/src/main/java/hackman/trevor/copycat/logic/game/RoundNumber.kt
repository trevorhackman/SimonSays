package hackman.trevor.copycat.logic.game

@JvmInline
value class RoundNumber(val roundNumber: Int) {
    companion object {
        val start = RoundNumber(1)
    }
}
