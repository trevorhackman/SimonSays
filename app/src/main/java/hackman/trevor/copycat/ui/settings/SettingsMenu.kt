package hackman.trevor.copycat.ui.settings

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import hackman.trevor.copycat.R
import hackman.trevor.copycat.observe
import hackman.trevor.copycat.system.displayHeight
import hackman.trevor.copycat.system.displayWidth
import hackman.trevor.copycat.system.sound.SoundManager
import hackman.trevor.copycat.ui.fadeIn
import hackman.trevor.copycat.ui.fadeOut
import kotlinx.android.synthetic.main.settings_menu.view.*
import kotlin.math.min

class SettingsMenu @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : ConstraintLayout(context, attributeSet), LifecycleOwner {

    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var lifecycleOwner: LifecycleOwner
    private lateinit var soundManager: SoundManager

    init {
        View.inflate(context, R.layout.settings_menu, this)
    }

    fun setup(
        settingsViewModel: SettingsViewModel,
        lifecycleOwner: LifecycleOwner,
        soundManager: SoundManager
    ) {
        this.settingsViewModel = settingsViewModel
        this.lifecycleOwner = lifecycleOwner
        this.soundManager = soundManager
        setupSpeedOption()
        setupColorOption()
        setupCloseButton()
        observeInBackground()
    }

    private fun setupSpeedOption() = settings_option_speed.setup(soundManager, settingsViewModel)

    private fun setupColorOption() = settings_option_color.setup(soundManager, settingsViewModel)

    private fun setupCloseButton() =
        settings_close_button.setOnClickListener {
            settingsViewModel.setInBackground(true)
            soundManager.click.play()
        }

    private fun observeInBackground() = observe(settingsViewModel.inBackground) {
        if (it) fadeOut()
        else fadeIn()
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        settings_option_speed.isEnabled = enabled
        settings_option_color.isEnabled = enabled
        settings_close_button.isEnabled = enabled
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.makeMeasureSpec(determineWidth(), MeasureSpec.EXACTLY)
        super.onMeasure(width, heightMeasureSpec)
    }

    private fun determineWidth() = min(displayWidth(), (displayWidth() + displayHeight()) / 2)

    override fun getLifecycle(): Lifecycle = lifecycleOwner.lifecycle
}
