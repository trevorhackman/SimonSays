package hackman.trevor.copycat.logic.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hackman.trevor.copycat.R
import hackman.trevor.copycat.logic.game.GameButton
import hackman.trevor.copycat.logic.game.GameMode
import hackman.trevor.copycat.logic.game.GameState
import hackman.trevor.copycat.logic.game.RoundNumber
import hackman.trevor.copycat.system.StringResource

class GameViewModelImpl : ViewModel(), GameViewModel {

    override val gameMode = MutableLiveData<GameMode>()

    override val gameState = MutableLiveData(GameState.MainMenu)

    override val roundNumber = MutableLiveData(RoundNumber.start)

    override val buttonPlayBack = MutableLiveData<GameButton?>()

    override val playerPushed = MutableLiveData<GameButton?>()

    override val infoText = MutableLiveData(StringResource(R.string.info_text_player1))
}

interface GameViewModel {
    val gameMode: MutableLiveData<GameMode>

    val gameState: MutableLiveData<GameState>

    val roundNumber: MutableLiveData<RoundNumber>

    // Indicates which button the game presses. Null indicates release.
    val buttonPlayBack: MutableLiveData<GameButton?>

    // Indicates player pressed a button. Null indicates no buttons are pressed.
    val playerPushed: MutableLiveData<GameButton?>

    val infoText: MutableLiveData<StringResource>
}
