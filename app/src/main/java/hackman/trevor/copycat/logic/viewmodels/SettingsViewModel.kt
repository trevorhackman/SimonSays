package hackman.trevor.copycat.logic.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hackman.trevor.copycat.logic.settings.ColorSet

class SettingsViewModelImpl : MenuViewModel(), SettingsViewModel {
    override val colorSet = MutableLiveData<ColorSet>()

    override fun setColorSet(colorSet: ColorSet) {
        this.colorSet.value = colorSet
    }
}

interface SettingsViewModel : Menu {
    val colorSet: LiveData<ColorSet>

    fun setColorSet(colorSet: ColorSet)
}
