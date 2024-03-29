package hackman.trevor.copycat.system.sound

import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import hackman.trevor.copycat.R
import hackman.trevor.copycat.system.log
import hackman.trevor.copycat.system.report

// Multiple instances of the same activity can momentarily exist, even with singleInstance launch mode.
// I found, rapidly closing/opening the app to reliably reproduce this. An activity can take time to be destroyed.
// A new instance of the same activity can be created without waiting for the last one to be destroyed.
// This results in two instances of the same activity, going through different lifecycle states, to exist simultaneously.
// This messes up and causes unexpected issues in a singleton lifecycle-dependent class like this. Tried on API 30.
// Fixed by removing old observer.
object SoundManager : DefaultLifecycleObserver {

    private var activity: AppCompatActivity? = null

    private var soundPool: SoundPool? = null

    /**
     * Volume levels
     * Balancing the volume out some b/c the higher pitched notes 'sound' louder than lower pitched notes
     */
    private const val VOLUME_CHIP1 = 0.54f
    private const val VOLUME_CHIP2 = 0.61f
    private const val VOLUME_CHIP3 = 0.68f
    private const val VOLUME_CHIP4 = 0.75f
    private const val VOLUME_FAILURE = 1.0f
    private const val VOLUME_CLICK = 0.3f

    // TODO Observe a view model and play sounds via it to remove dependency
    private val allSounds = mutableListOf<SoundImpl>()
    val chip1 = createSound(R.raw.chip1_amp, VOLUME_CHIP1) // Sound 1 (Highest)
    val chip2 = createSound(R.raw.chip2_amp, VOLUME_CHIP2) // Sound 2
    val chip3 = createSound(R.raw.chip3_amp, VOLUME_CHIP3) // Sound 3
    val chip4 = createSound(R.raw.chip4_amp, VOLUME_CHIP4) // Sound 4 (Lowest)
    val failure_error = createSound(R.raw.failure_error, VOLUME_FAILURE) // Fail sound
    val failure_bit = createSound(R.raw.failure_bit, VOLUME_FAILURE) // Fail sound
    val click = createSound(R.raw.click, VOLUME_CLICK) // Button click sound

    fun stopAllSounds() {
        soundPool?.autoPause()
    }

    fun setup(activity: AppCompatActivity) {
        this.activity?.lifecycle?.removeObserver(this) // Necessary to fix vulnerabilities from multiple activities
        this.activity = activity
        activity.lifecycle.addObserver(this)
    }

    override fun onCreate(owner: LifecycleOwner) {
        buildSoundPoolIfNeeded()
    }

    override fun onStart(owner: LifecycleOwner) {
        buildSoundPoolIfNeeded()
        enableAllSounds()
    }

    override fun onResume(owner: LifecycleOwner) {
        buildSoundPoolIfNeeded()
    }

    override fun onStop(owner: LifecycleOwner) {
        releaseResources(initial = true)
        disableAllSounds()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        releaseResources(initial = false) // For safety
        activity = null
    }

    private fun buildSoundPoolIfNeeded() {
        if (soundPool == null) {
            soundPool = SoundPool.Builder().setMaxStreams(20).build()
            updateSounds()
        }
    }

    private fun updateSounds() = allSounds.forEach { it.soundId = loadSoundAndGetId(it.resource) }

    private fun createSound(resource: Int, volume: Float): Sound = SoundImpl(resource, volume).also {
        allSounds.add(it)
    }

    private fun loadSoundAndGetId(resource: Int): Int =
        soundPool?.load(activity, resource, 1) ?: run {
            report("Attempted to load sound on null SoundPool")
            0
        }

    /**
     * Release all sound resources
     * Don't hog resources from other apps when not in front; don't leak memory and stop sound errors
     */
    private fun releaseResources(initial: Boolean) {
        soundPool?.let {
            if (!initial) report("Initial release didn't null SoundPool")
            unloadAllSounds()
            it.release()
            soundPool = null
        } ?: run {
            if (initial) report("Attempted to release on null SoundPool")
        }
    }

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

    private class SoundImpl(
        val resource: Int,
        val volume: Float
    ) : Sound {
        var soundId = 0
        var isEnabled = true

        override fun play() {
            if (isEnabled) playSound(this)
        }
    }
}
