package hackman.trevor.copycat

import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import hackman.trevor.copycat.system.ads.AdManager
import hackman.trevor.copycat.system.sound.SoundManager

fun MainActivity.injector(): Lazy<Injector> = this.viewModels<InjectorViewModel>()
fun BaseFragment.soundProvider(): Lazy<SoundProvider> = this.activityViewModels<InjectorViewModel>()
fun BaseFragment.adProvider(): Lazy<AdProvider> = this.activityViewModels<InjectorViewModel>()

private class InjectorViewModel : ViewModel(), Injector, SoundProvider, AdProvider {
    override lateinit var ads: AdManager
    override lateinit var sounds: SoundManager

    override fun inject(ads: AdManager, sounds: SoundManager) {
        this.ads = ads
        this.sounds = sounds
    }
}

interface Injector {
    fun inject(ads: AdManager, sounds: SoundManager)
}

interface SoundProvider {
    val sounds: SoundManager
}

interface AdProvider {
    val ads: AdManager
}
