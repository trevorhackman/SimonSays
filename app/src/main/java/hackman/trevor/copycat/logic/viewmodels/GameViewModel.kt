package hackman.trevor.copycat.logic.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hackman.trevor.copycat.logic.game.GameMode
import hackman.trevor.copycat.logic.game.GameState

class GameViewModelImpl : ViewModel(), GameViewModel {
    override val gameMode = MutableLiveData<GameMode>()

    override val gameState = MutableLiveData(GameState.MainMenu)

    override val roundNumber = MutableLiveData(1)

    override fun setGameMode(gameMode: GameMode) {
        this.gameMode.value = gameMode
    }

    override fun playButtonClicked() {
        gameState.value = if (gameState.value == GameState.MainMenu) GameState.Watch else GameState.MainMenu
    }
}

interface GameViewModel {
    val gameMode: LiveData<GameMode>

    val gameState: LiveData<GameState>

    val roundNumber: LiveData<Int>

    fun setGameMode(gameMode: GameMode)

    fun playButtonClicked()
}
