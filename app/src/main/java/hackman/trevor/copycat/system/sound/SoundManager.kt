package hackman.trevor.copycat.system.sound

import android.media.SoundPool
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import hackman.trevor.copycat.MainActivity
import hackman.trevor.copycat.R
import hackman.trevor.copycat.system.log
import hackman.trevor.copycat.system.report

class SoundManager(
    private val mainActivity: MainActivity
) : LifecycleObserver {
    private var soundPool: SoundPool? = null

    private val allSounds = mutableListOf<SoundImpl>()
    val chip1 = createSound(R.raw.chip1_amp, VOLUME_CHIP1) // Sound 1 (Highest)
    val chip2 = createSound(R.raw.chip2_amp, VOLUME_CHIP2) // Sound 2
    val chip3 = createSound(R.raw.chip3_amp, VOLUME_CHIP3) // Sound 3
    val chip4 = createSound(R.raw.chip4_amp, VOLUME_CHIP4) // Sound 4 (Lowest)
    val failure = createSound(R.raw.failure, VOLUME_FAILURE) // Fail sound
    val click = createSound(R.raw.click, VOLUME_CLICK) // Button click sound

    init {
        mainActivity.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(owner: LifecycleOwner) {
        buildSoundPoolIfNeeded()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart(owner: LifecycleOwner) {
        buildSoundPoolIfNeeded()
        enableAllSounds()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume(owner: LifecycleOwner) {
        buildSoundPoolIfNeeded()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop(owner: LifecycleOwner) {
        releaseResources()
        disableAllSounds()
    }

    private fun buildSoundPoolIfNeeded() {
        if (soundPool == null) {
            soundPool = SoundPool.Builder().setMaxStreams(20).build()
            updateSounds()
        }
    }

    private fun updateSounds() =
        allSounds.forEach {
            it.soundId = loadSoundAndGetId(it.resource)
        }

    private fun createSound(resource: Int, volume: Float): Sound =
        SoundImpl(resource, volume, this).also {
            allSounds.add(it)
        }

    private fun loadSoundAndGetId(resource: Int): Int =
        soundPool?.load(mainActivity, resource, 1) ?: {
            report("Attempted to load sound on null SoundPool")
            0
        }.invoke()

    /**
     * Release all sound resources
     * Don't hog resources from other apps when not in front; don't leak memory and stop sound errors
     */
    private fun releaseResources() =
        soundPool?.let {
            unloadAllSounds()
            it.release()
            soundPool = null
        } ?: report("Attempted to release on null SoundPool")

    private fun unloadAllSounds() = allSounds.forEach { unloadSound(it) }

    private fun unloadSound(sound: SoundImpl) =
        soundPool?.unload(sound.soundId) ?: report("Attempted to unload sound on null SoundPool")

    private fun playSound(sound: SoundImpl) {
        soundPool?.let {
            val streamId = soundPool?.play(sound.soundId, sound.volume, sound.volume, 0, 0, 1f)
            if (streamId == 0) log("Sound failed to play")
            else log("Sound played with stream id $streamId")
        } ?: report("Attempted to play sound on null SoundPool")
    }

    private fun enableAllSounds() = allSounds.forEach { it.isEnabled = true }

    private fun disableAllSounds() = allSounds.forEach { it.isEnabled = false }

    companion object {
        /**
         * Volume levels
         * Balancing the volume out some b/c the higher pitched notes 'sound' louder than lower pitched notes
         */
        private const val VOLUME_CHIP1 = 0.3f
        private const val VOLUME_CHIP2 = 0.45f
        private const val VOLUME_CHIP3 = 0.6f
        private const val VOLUME_CHIP4 = 0.75f
        private const val VOLUME_FAILURE = 1.0f
        private const val VOLUME_CLICK = 0.3f
    }

    private class SoundImpl(
        val resource: Int,
        val volume: Float,
        val soundManager: SoundManager
    ) : Sound {
        var soundId = 0
        var isEnabled = true

        override fun play() {
            if (isEnabled) soundManager.playSound(this)
        }
    }
}
