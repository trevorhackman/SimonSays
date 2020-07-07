package hackman.trevor.copycat.logic.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hackman.trevor.copycat.logic.settings.ColorSet

class SettingsViewModelImpl : ViewModel(), SettingsViewModel {
    override val colorSet = MutableLiveData<ColorSet>()

    override fun setColorSet(colorSet: ColorSet) {
        this.colorSet.value = colorSet
    }

    override val inBackground: MutableLiveData<Boolean> = MutableLiveData(true)

    override fun setInBackground(inBackground: Boolean) {
        if (this.inBackground.value != inBackground)
            this.inBackground.value = inBackground
    }
}

interface SettingsViewModel {
    val colorSet: LiveData<ColorSet>

    fun setColorSet(colorSet: ColorSet)

    val inBackground: LiveData<Boolean>

    fun setInBackground(inBackground: Boolean)
}
