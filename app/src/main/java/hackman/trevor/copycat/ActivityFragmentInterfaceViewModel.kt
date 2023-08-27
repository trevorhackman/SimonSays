package hackman.trevor.copycat

import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

fun MainActivity.activityInterface(): Lazy<ActivityInterface> = viewModels<ActivityFragmentInterfaceViewModel>()
fun Fragment.fragmentInterface(): Lazy<FragmentInterface> = activityViewModels<ActivityFragmentInterfaceViewModel>()

class ActivityFragmentInterfaceViewModel : ViewModel(), ActivityInterface, FragmentInterface {
    override var onBackPressed: (() -> BackEvent)? = null

    override fun setBackBehavior(onBackPressed: (() -> BackEvent)?) {
        this.onBackPressed = onBackPressed
    }

    override val callSuper = MutableLiveData(false)

    override fun callSuper() {
        callSuper.value = true
        callSuper.value = false
    }

    override val requestedOrientation = MutableLiveData(Orientation.User)

    override fun setOrientation(orientation: Orientation) {
        requestedOrientation.value = orientation
    }
}

interface ActivityInterface {
    val onBackPressed: (() -> BackEvent)?

    val callSuper: LiveData<Boolean>

    val requestedOrientation: LiveData<Orientation>
}

interface FragmentInterface {
    /**
     * What will happen when Android's back button is pressed
     *
     * @param onBackPressed
     * On a return of [BackEvent.Consumed], the back event is consumed, else the activity's super.onBackPressed will be called
     */
    fun setBackBehavior(onBackPressed: (() -> BackEvent)?)

    // Triggers the activity's super.onBackPressed()
    fun callSuper()

    // Set the requested orientation of the device
    fun setOrientation(orientation: Orientation)
}

@JvmInline
@Suppress("NON_PUBLIC_PRIMARY_CONSTRUCTOR_OF_INLINE_CLASS")
value class BackEvent private constructor(val consumed: Boolean) {
    companion object {
        val Consumed = BackEvent(true)
        val CallSuper = BackEvent(false)
    }
}

enum class Orientation { User, Locked }
