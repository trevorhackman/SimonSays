package hackman.trevor.copycat

import androidx.lifecycle.ViewModel
import hackman.trevor.copycat.system.ads.AdManager
import hackman.trevor.copycat.system.sound.SoundManager

class InjectorViewModel : ViewModel(), Inject, SoundProvider, AdProvider {
    override lateinit var ads: AdManager
    override lateinit var sounds: SoundManager

    override fun inject(ads: AdManager, sounds: SoundManager) {
        this.ads = ads
        this.sounds = sounds
    }
}

interface Inject {
    fun inject(ads: AdManager, sounds: SoundManager)
}

interface SoundProvider {
    val sounds: SoundManager
}

interface AdProvider {
    val ads: AdManager
}
