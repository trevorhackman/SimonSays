package hackman.trevor.copycat.logic.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FailureViewModelImpl : ViewModel(), FailureViewModel {
    override val inBackground: MutableLiveData<Boolean> = MutableLiveData(true)

    override fun setInBackground(inBackground: Boolean) {
        if (this.inBackground.value != inBackground)
            this.inBackground.value = inBackground
    }
}

interface FailureViewModel : Menu {
    // TODO
}
