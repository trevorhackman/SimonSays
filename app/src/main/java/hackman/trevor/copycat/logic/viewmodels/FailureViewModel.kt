package hackman.trevor.copycat.logic.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hackman.trevor.copycat.R
import hackman.trevor.copycat.logic.game.GameButton
import hackman.trevor.copycat.logic.game.GameMode
import hackman.trevor.copycat.logic.game.Score
import hackman.trevor.copycat.logic.game.TwoPlayerVictor
import hackman.trevor.copycat.system.StringResource
import hackman.trevor.copycat.ui.game_modes.name

class FailureViewModelImpl : MenuViewModel(), FailureViewModel {
    override val topTextField = MutableLiveData<StringResource>()

    override val topTextValue = MutableLiveData<StringResource>()

    override fun setMode(mode: GameMode) {
        topTextField.value = StringResource(R.string.failure_row_mode)
        topTextValue.value = mode.name()
    }

    override fun setTwoPlayerVictor(victor: TwoPlayerVictor) {
        topTextField.value = StringResource(R.string.failure_row_victor)
        topTextValue.value = victor.name()
    }

    override val score = MutableLiveData<Score>()

    override val best = MutableLiveData<Score>()

    override val pressed = MutableLiveData<GameButton?>()

    override val correct = MutableLiveData<GameButton?>()
}

interface FailureViewModel : Menu {
    val topTextField: LiveData<StringResource>

    val topTextValue: LiveData<StringResource>

    fun setMode(mode: GameMode)

    fun setTwoPlayerVictor(victor: TwoPlayerVictor)

    val score: MutableLiveData<Score>

    val best: MutableLiveData<Score>

    val pressed: MutableLiveData<GameButton?>

    val correct: MutableLiveData<GameButton?>
}

private fun TwoPlayerVictor.name() = StringResource(
    when(this) {
        TwoPlayerVictor.Player1 -> R.string.failure_player1_wins
        TwoPlayerVictor.Player2 -> R.string.failure_player2_wins
        TwoPlayerVictor.Tie -> R.string.failure_players_tie
    }
)
