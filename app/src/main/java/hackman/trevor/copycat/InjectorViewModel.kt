package hackman.trevor.copycat

import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import hackman.trevor.copycat.system.billing.BillingManager
import hackman.trevor.copycat.system.sound.SoundManager

fun MainActivity.injector(): Lazy<ActivityInterface> = viewModels<InjectorViewModel>()
fun BaseFragment.soundProvider(): Lazy<SoundProvider> = activityViewModels<InjectorViewModel>()
fun BaseFragment.billingProvider(): Lazy<BillingProvider> = activityViewModels<InjectorViewModel>()
fun BaseFragment.onBackPressed(): Lazy<OnBackPressed> = activityViewModels<InjectorViewModel>()

class InjectorViewModel : ViewModel(), ActivityInterface, SoundProvider, BillingProvider, OnBackPressed {
    override lateinit var soundManager: SoundManager
    override lateinit var billingManager: BillingManager

    override var onBackPressed: (() -> Boolean)? = null

    override fun inject(
        soundManager: SoundManager,
        billingManager: BillingManager
    ) {
        this.soundManager = soundManager
        this.billingManager = billingManager
    }

    override fun setBehavior(onBackPressed: (() -> Boolean)?) {
        this.onBackPressed = onBackPressed
    }
}

interface ActivityInterface {
    fun inject(soundManager: SoundManager, billingManager: BillingManager)

    val onBackPressed: (() -> Boolean)?
}

interface SoundProvider {
    val soundManager: SoundManager
}

interface BillingProvider {
    val billingManager: BillingManager
}

interface OnBackPressed {
    /**
     * What will happen when Android's back button is pressed
     *
     * @param onBackPressed
     * On a null value or a return of true, the activity's super.onBackPressed() will be called
     */
    fun setBehavior(onBackPressed: (() -> Boolean)?)
}
