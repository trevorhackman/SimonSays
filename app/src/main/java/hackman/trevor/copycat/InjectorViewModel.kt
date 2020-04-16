package hackman.trevor.copycat

import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import hackman.trevor.copycat.system.ads.AdManager
import hackman.trevor.copycat.system.billing.BillingManager
import hackman.trevor.copycat.system.sound.SoundManager

fun MainActivity.injector(): Lazy<ActivityInterface> = viewModels<InjectorViewModel>()
fun BaseFragment.soundProvider(): Lazy<SoundProvider> = activityViewModels<InjectorViewModel>()
fun BaseFragment.adProvider(): Lazy<AdProvider> = activityViewModels<InjectorViewModel>()
fun BaseFragment.billingProvider(): Lazy<BillingProvider> = activityViewModels<InjectorViewModel>()
fun BaseFragment.onBackPressed(): Lazy<OnBackPressed> = activityViewModels<InjectorViewModel>()

class InjectorViewModel : ViewModel(), ActivityInterface, SoundProvider, AdProvider,
    BillingProvider, OnBackPressed {
    override lateinit var adManager: AdManager
    override lateinit var soundManager: SoundManager
    override lateinit var billingManager: BillingManager

    override var onBackPressed: (() -> Boolean)? = null

    override fun inject(
        adManager: AdManager,
        soundManager: SoundManager,
        billingManager: BillingManager
    ) {
        this.adManager = adManager
        this.soundManager = soundManager
        this.billingManager = billingManager
    }

    override fun injectOnBackPressed(onBackPressed: (() -> Boolean)?) {
        this.onBackPressed = onBackPressed
    }
}

interface ActivityInterface {
    fun inject(adManager: AdManager, soundManager: SoundManager, billingManager: BillingManager)

    val onBackPressed: (() -> Boolean)?
}

interface SoundProvider {
    val soundManager: SoundManager
}

interface AdProvider {
    val adManager: AdManager
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
    fun injectOnBackPressed(onBackPressed: (() -> Boolean)?)
}
