package hackman.trevor.copycat.ui.settings

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import hackman.trevor.copycat.databinding.SettingsMenuBinding
import hackman.trevor.copycat.logic.viewmodels.SettingsViewModel
import hackman.trevor.copycat.observe
import hackman.trevor.copycat.system.displayHeight
import hackman.trevor.copycat.system.displayWidth
import hackman.trevor.copycat.system.sound.SoundManager
import hackman.trevor.copycat.ui.fadeIn
import hackman.trevor.copycat.ui.fadeOut
import kotlin.math.min

class SettingsMenu @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : FrameLayout(context, attributeSet), LifecycleOwner {

    private lateinit var settingsViewModel: SettingsViewModel
    override lateinit var lifecycle: Lifecycle

    private val binding = SettingsMenuBinding.inflate(LayoutInflater.from(context), this, true)

    fun setup(
        settingsViewModel: SettingsViewModel,
        lifecycle: Lifecycle
    ) {
        this.settingsViewModel = settingsViewModel
        this.lifecycle = lifecycle
        setupSpeedOption()
        setupColorOption()
        setupFailureOption()
        setOnCloseClickListener()
        observeInBackground()
    }

    private fun setupSpeedOption() = binding.settingsOptionSpeed.setup(settingsViewModel)

    private fun setupColorOption() = binding.settingsOptionColor.setup(settingsViewModel)

    private fun setupFailureOption() = binding.settingsOptionFailure.setup(settingsViewModel)

    private fun setOnCloseClickListener() = binding.settingsCloseButton.setOnClickListener {
        settingsViewModel.setInBackground(true)
        SoundManager.click.play()
    }

    private fun observeInBackground() = observe(settingsViewModel.inBackground) {
        if (it) fadeOut()
        else fadeIn(startAction = { settingsViewModel.isAnimatingIn = true }) {
            settingsViewModel.isAnimatingIn = false
        }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        binding.settingsOptionSpeed.isEnabled = enabled
        binding.settingsOptionColor.isEnabled = enabled
        binding.settingsOptionFailure.isEnabled = enabled
        binding.settingsCloseButton.isEnabled = enabled
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.makeMeasureSpec(determineWidth(), MeasureSpec.EXACTLY)
        super.onMeasure(width, heightMeasureSpec)
    }

    private fun determineWidth() = min(displayWidth(), (displayWidth() + displayHeight()) / 2)
}
