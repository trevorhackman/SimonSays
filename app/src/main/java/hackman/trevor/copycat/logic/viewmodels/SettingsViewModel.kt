package hackman.trevor.copycat.logic.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hackman.trevor.copycat.logic.settings.ColorSet
import hackman.trevor.copycat.logic.settings.FailureSound

class SettingsViewModelImpl : MenuViewModel(), SettingsViewModel {
    override val colorSet = MutableLiveData<ColorSet>()

    override fun setColorSet(colorSet: ColorSet) {
        this.colorSet.value = colorSet
    }

    override val failureSound = MutableLiveData<FailureSound>()

    override fun setFailureSound(failureSound: FailureSound) {
        this.failureSound.value = failureSound
    }
}

interface SettingsViewModel : Menu {
    val colorSet: LiveData<ColorSet>

    fun setColorSet(colorSet: ColorSet)

    val failureSound: LiveData<FailureSound>

    fun setFailureSound(failureSound: FailureSound)
}
