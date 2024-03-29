package hackman.trevor.copycat.logic.game

import hackman.trevor.copycat.system.SaveData

@JvmInline
value class Score(val score: Int) {
    companion object {
        fun getHighScore(gameMode: GameMode) = Score(SaveData.getHighScore(gameMode))
    }
}
