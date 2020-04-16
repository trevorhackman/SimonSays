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
    override lateinit var ads: AdManager
    override lateinit var sounds: SoundManager
    override lateinit var billing: BillingManager

    override fun inject(ads: AdManager, sounds: SoundManager, billing: BillingManager) {
        this.ads = ads
        this.sounds = sounds
        this.billing = billing
    }
}

interface Injector {
    fun inject(ads: AdManager, sounds: SoundManager, billing: BillingManager)
}

interface SoundProvider {
    val sounds: SoundManager
}

interface AdProvider {
    val ads: AdManager
}

interface BillingProvider {
    val billing: BillingManager
}
