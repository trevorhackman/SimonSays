package hackman.trevor.copycat

import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import hackman.trevor.copycat.system.ads.AdManager
import hackman.trevor.copycat.system.billing.BillingManager
import hackman.trevor.copycat.system.sound.SoundManager

fun MainActivity.injector(): Lazy<Injector> = this.viewModels<InjectorViewModel>()
fun BaseFragment.soundProvider(): Lazy<SoundProvider> = this.activityViewModels<InjectorViewModel>()
fun BaseFragment.adProvider(): Lazy<AdProvider> = this.activityViewModels<InjectorViewModel>()
fun BaseFragment.billingProvider(): Lazy<BillingProvider> =
    this.activityViewModels<InjectorViewModel>()

class InjectorViewModel : ViewModel(), Injector, SoundProvider, AdProvider, BillingProvider {
    override lateinit var adManager: AdManager
    override lateinit var soundManager: SoundManager
    override lateinit var billingManager: BillingManager

    override fun inject(
        adManager: AdManager,
        soundManager: SoundManager,
        billingManager: BillingManager
    ) {
        this.adManager = adManager
        this.soundManager = soundManager
        this.billingManager = billingManager
    }
}

interface Injector {
    fun inject(adManager: AdManager, soundManager: SoundManager, billingManager: BillingManager)
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
