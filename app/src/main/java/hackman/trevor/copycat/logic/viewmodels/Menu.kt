package hackman.trevor.copycat.logic.viewmodels

import androidx.lifecycle.LiveData

interface Menu {
    val inBackground: LiveData<Boolean>

    fun setInBackground(inBackground: Boolean)
}
