package hackman.trevor.copycat.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hackman.trevor.copycat.logic.enums.ColorSet

class SettingsViewModel : ViewModel() {
    private val _colorSet = MutableLiveData<ColorSet>()
    val colorSet: LiveData<ColorSet> = _colorSet

    fun setColorSet(colorSet: ColorSet) {
        _colorSet.value = colorSet
    }

    private val _inBackground = MutableLiveData<Boolean>(false)
    val inBackground: LiveData<Boolean> = _inBackground

    fun setInBackground(inBackground: Boolean) {
        if (this.inBackground.value != inBackground)
            _inBackground.value = inBackground
    }
}