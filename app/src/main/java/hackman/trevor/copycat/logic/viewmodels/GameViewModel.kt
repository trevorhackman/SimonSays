package hackman.trevor.copycat.logic.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hackman.trevor.copycat.logic.game.GameButton
import hackman.trevor.copycat.logic.game.GameMode
import hackman.trevor.copycat.logic.game.GameState
import hackman.trevor.copycat.logic.game.RoundNumber

class GameViewModelImpl : ViewModel(), GameViewModel {

    override val gameMode = MutableLiveData<GameMode>()

    override val gameState = MutableLiveData(GameState.MainMenu)

    override val roundNumber = MutableLiveData(RoundNumber(1))

    override val buttonPlayBack = MutableLiveData<GameButton>()

    override val playerPushed = MutableLiveData<GameButton>()

    override fun setGameMode(gameMode: GameMode) {
        this.gameMode.value = gameMode
    }

    override fun setGameState(gameState: GameState) {
        this.gameState.value = gameState
    }

    override fun setRoundNumber(roundNumber: RoundNumber) {
        this.roundNumber.value = roundNumber
    }

    override fun setButtonPlayBack(gameButton: GameButton?) {
        this.buttonPlayBack.value = gameButton
    }

    override fun setPlayerPushed(gameButton: GameButton?) {
        this.playerPushed.value = gameButton
    }
}

interface GameViewModel {
    val gameMode: LiveData<GameMode>

    val gameState: LiveData<GameState>

    val roundNumber: LiveData<RoundNumber>

    /** Indicates which button the game presses. Null indicates release. */
    val buttonPlayBack: LiveData<GameButton?>

    /** Indicates player pressed a button. Null indicates no buttons are pressed. */
    val playerPushed: LiveData<GameButton?>

    fun setGameMode(gameMode: GameMode)

    fun setGameState(gameState: GameState)

    fun setRoundNumber(roundNumber: RoundNumber)

    fun setButtonPlayBack(gameButton: GameButton?)

    fun setPlayerPushed(gameButton: GameButton?)
}
