package hackman.trevor.copycat

import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel

fun MainActivity.activityInterface(): Lazy<ActivityInterface> = viewModels<ActivityFragmentInterfaceViewModel>()
fun BaseFragment.onBackPressed(): Lazy<OnBackPressed> = activityViewModels<ActivityFragmentInterfaceViewModel>()

class ActivityFragmentInterfaceViewModel : ViewModel(), ActivityInterface, OnBackPressed {
    override var onBackPressed: (() -> Boolean)? = null

    override fun setBehavior(onBackPressed: (() -> Boolean)?) {
        this.onBackPressed = onBackPressed
    }
}

interface ActivityInterface {
    val onBackPressed: (() -> Boolean)?
}

interface OnBackPressed {
    /**
     * What will happen when Android's back button is pressed
     *
     * @param onBackPressed
     * On a return of true, the back event is consumed, else the activity's super.onBackPressed will be called
     */
    fun setBehavior(onBackPressed: (() -> Boolean)?)
}
