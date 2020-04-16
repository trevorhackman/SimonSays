package hackman.trevor.copycat.ui.settings

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import hackman.trevor.copycat.R
import hackman.trevor.copycat.observe
import hackman.trevor.copycat.system.displayMinimum
import hackman.trevor.copycat.system.sound.SoundManager
import hackman.trevor.copycat.ui.fadeIn
import hackman.trevor.copycat.ui.fadeOut
import kotlinx.android.synthetic.main.settings_menu.view.*

class SettingsMenu @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : ConstraintLayout(context, attributeSet), LifecycleOwner {

    lateinit var settingsViewModel: SettingsViewModel
    lateinit var lifecycleOwner: LifecycleOwner
    lateinit var soundManager: SoundManager

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
        bindSpeedOption()
        bindColorOption()
        bindCloseButton()
        observeInBackground()
    }

    private fun bindSpeedOption() =
        settings_option_speed.setOnChangeListener {
            soundManager.click.play()
        }

    private fun bindColorOption() =
        settings_option_color.setOnChangeListener {
            settingsViewModel.setColorSet(it)
            soundManager.click.play()
        }

    private fun bindCloseButton() =
        settings_close_button.setOnClickListener {
            settingsViewModel.setInBackground(true)
            soundManager.click.play()
        }

    private fun observeInBackground() = observe(settingsViewModel.inBackground) {
        if (it) fadeOut()
        else fadeIn()
    }

    private fun fadeIn() = fadeIn({ fadeInStartAction() }, { fadeInEndAction() })

    private fun fadeInStartAction() {
        settingsViewModel.setInBackground(false)
    }

    private fun fadeInEndAction() {
        isEnabled = true
    }

    private fun fadeOut() = fadeOut({ fadeOutStartAction() }, { fadeOutEndAction() })

    private fun fadeOutStartAction() {
        isEnabled = false
    }

    private fun fadeOutEndAction() {
        settingsViewModel.setInBackground(true)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        settings_option_speed.isEnabled = enabled
        settings_option_color.isEnabled = enabled
        settings_close_button.isEnabled = enabled
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.makeMeasureSpec(displayMinimum(), MeasureSpec.EXACTLY)
        super.onMeasure(width, heightMeasureSpec)
    }

    override fun getLifecycle(): Lifecycle = lifecycleOwner.lifecycle
}