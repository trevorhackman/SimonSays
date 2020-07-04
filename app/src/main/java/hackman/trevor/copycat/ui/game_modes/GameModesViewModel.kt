package hackman.trevor.copycat.ui.game_modes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hackman.trevor.copycat.logic.game.GameMode

class GameModesViewModelImpl : ViewModel(), GameModesViewModel {
    override val gameMode = MutableLiveData<GameMode>()

    override fun setGameMode(gameMode: GameMode) {
        this.gameMode.value = gameMode
    }

    override val inBackground: MutableLiveData<Boolean> = MutableLiveData(true)

    override fun setInBackground(inBackground: Boolean) {
        if (this.inBackground.value != inBackground)
            this.inBackground.value = inBackground
    }
}

interface GameModesViewModel {
    val gameMode: LiveData<GameMode>

    fun setGameMode(gameMode: GameMode)

    val inBackground: LiveData<Boolean>

    fun setInBackground(inBackground: Boolean)
}
