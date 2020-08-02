package hackman.trevor.copycat.logic.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hackman.trevor.copycat.logic.game.GameButton
import hackman.trevor.copycat.logic.game.GameMode
import hackman.trevor.copycat.logic.game.Score

class FailureViewModelImpl : MenuViewModel(), FailureViewModel {
    override val mode = MutableLiveData<GameMode>()

    override fun setMode(mode: GameMode) {
        this.mode.value = mode
    }

    override val score = MutableLiveData<Score>()

    override fun setScore(score: Score) {
        this.score.value = score
    }

    override val best = MutableLiveData<Score>()

    override fun setBest(best: Score) {
        this.best.value = best
    }

    override val pressed = MutableLiveData<GameButton>()

    override fun setPressed(pressed: GameButton?) {
        this.pressed.value = pressed
    }

    override val correct = MutableLiveData<GameButton>()

    override fun setCorrect(correct: GameButton?) {
        this.correct.value = correct
    }
}

interface FailureViewModel : Menu {
    val mode: LiveData<GameMode>

    fun setMode(mode: GameMode)

    val score: LiveData<Score>

    fun setScore(score: Score)

    val best: LiveData<Score>

    fun setBest(best: Score)

    val pressed: LiveData<GameButton?>

    fun setPressed(pressed: GameButton?)

    val correct: LiveData<GameButton?>

    fun setCorrect(correct: GameButton?)
}
