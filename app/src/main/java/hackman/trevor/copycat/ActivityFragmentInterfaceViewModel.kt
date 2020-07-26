package hackman.trevor.copycat

import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

fun MainActivity.activityInterface(): Lazy<ActivityInterface> = viewModels<ActivityFragmentInterfaceViewModel>()
fun BaseFragment.onBackPressed(): Lazy<OnBackPressed> = activityViewModels<ActivityFragmentInterfaceViewModel>()

class ActivityFragmentInterfaceViewModel : ViewModel(), ActivityInterface, OnBackPressed {
    override var onBackPressed: (() -> BackEvent)? = null

    override fun setBehavior(onBackPressed: (() -> BackEvent)?) {
        this.onBackPressed = onBackPressed
    }

    override val callSuper = MutableLiveData(false)

    override fun callSuper() {
        callSuper.value = true
        callSuper.value = false
    }
}

interface ActivityInterface {
    val onBackPressed: (() -> BackEvent)?

    val callSuper: LiveData<Boolean>
}

interface OnBackPressed {
    /**
     * What will happen when Android's back button is pressed
     *
     * @param onBackPressed
     * On a return of [BackEvent.Consumed], the back event is consumed, else the activity's super.onBackPressed will be called
     */
    fun setBehavior(onBackPressed: (() -> BackEvent)?)

    // Triggers the activity's super.onBackPressed()
    fun callSuper()
}

@Suppress("NON_PUBLIC_PRIMARY_CONSTRUCTOR_OF_INLINE_CLASS")
inline class BackEvent private constructor(val consumed: Boolean) {
    companion object {
        val Consumed = BackEvent(true)
        val CallSuper = BackEvent(false)
    }
}
