package hackman.trevor.copycat.logic.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class MenuViewModel : ViewModel(), Menu {
    override var isAnimatingIn = false

    override val inBackground = MutableLiveData(true)

    override fun setInBackground(inBackground: Boolean) {
        if (this.inBackground.value != inBackground)
            this.inBackground.value = inBackground
    }
}

interface Menu {
    var isAnimatingIn: Boolean

    val inBackground: LiveData<Boolean>

    // TODO, weird ugly way of saying show() and hide()
    fun setInBackground(inBackground: Boolean)
}
