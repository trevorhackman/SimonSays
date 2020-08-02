package hackman.trevor.copycat.logic.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hackman.trevor.copycat.logic.game.GameMode

class GameModesViewModelImpl : MenuViewModel(), GameModesViewModel {
    override val gameMode = MutableLiveData<GameMode>()

    override fun setGameMode(gameMode: GameMode) {
        this.gameMode.value = gameMode
    }
}

interface GameModesViewModel : Menu {
    val gameMode: LiveData<GameMode>

    fun setGameMode(gameMode: GameMode)
}
