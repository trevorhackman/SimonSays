package hackman.trevor.copycat.ui.color_button

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import hackman.trevor.copycat.R
import hackman.trevor.copycat.logic.viewmodels.GameViewModel
import hackman.trevor.copycat.logic.viewmodels.SettingsViewModel
import hackman.trevor.copycat.observe
import hackman.trevor.copycat.system.sound.SoundManager
import kotlinx.android.synthetic.main.color_grid.view.*

class ColorButtonsLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : ConstraintLayout(context, attributeSet), LifecycleOwner {

    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var gameViewModel: GameViewModel
    private lateinit var lifecycle: Lifecycle

    init {
        View.inflate(context, R.layout.color_grid, this)
    }

    private val buttons = listOf(
        color_button_top_left,
        color_button_top_right,
        color_button_bottom_left,
        color_button_bottom_right
    )

    fun setup(settingsViewModel: SettingsViewModel, gameViewModel: GameViewModel, lifecycle: Lifecycle) {
        this.settingsViewModel = settingsViewModel
        this.gameViewModel = gameViewModel
        this.lifecycle = lifecycle
        setupColorButtons()
        observeColorSettings()
        observeButtonPlayBack()
    }

    private fun setupColorButtons() {
        color_button_top_left.sound = SoundManager.chip1
        color_button_top_right.sound = SoundManager.chip2
        color_button_bottom_left.sound = SoundManager.chip3
        color_button_bottom_right.sound = SoundManager.chip4
    }

    private fun observeColorSettings() = observe(settingsViewModel.colorSet) {
        color_button_top_left.setColorResource(it.colors.topLeft)
        color_button_top_right.setColorResource(it.colors.topRight)
        color_button_bottom_left.setColorResource(it.colors.bottomLeft)
        color_button_bottom_right.setColorResource(it.colors.bottomRight)
    }

    private fun observeButtonPlayBack() = observe(gameViewModel.buttonPlayBack) {
        when (it) {
            null -> buttons.forEach(ColorButton::release)
            else -> buttons[it.buttonNumber].press()
        }
    }

    override fun getLifecycle(): Lifecycle = lifecycle
}
