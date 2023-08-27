package hackman.trevor.copycat.ui.color_button

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import hackman.trevor.copycat.databinding.ColorGridBinding
import hackman.trevor.copycat.logic.game.GameButton
import hackman.trevor.copycat.logic.game.GameState
import hackman.trevor.copycat.logic.viewmodels.GameViewModel
import hackman.trevor.copycat.logic.viewmodels.SettingsViewModel
import hackman.trevor.copycat.observe
import hackman.trevor.copycat.system.sound.SoundManager

class ColorButtonsLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : FrameLayout(context, attributeSet), LifecycleOwner {

    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var gameViewModel: GameViewModel
    override lateinit var lifecycle: Lifecycle

    private val binding = ColorGridBinding.inflate(LayoutInflater.from(context), this, true)

    private val buttons = listOf(
        binding.colorButtonTopLeft,
        binding.colorButtonTopRight,
        binding.colorButtonBottomLeft,
        binding.colorButtonBottomRight
    )

    fun setup(settingsViewModel: SettingsViewModel, gameViewModel: GameViewModel, lifecycle: Lifecycle) {
        this.settingsViewModel = settingsViewModel
        this.gameViewModel = gameViewModel
        this.lifecycle = lifecycle
        setupInfoText()
        setupMainButton()
        setSounds()
        setListeners()
        observeColorSettings()
        observeButtonPlayBack()
        observeGameState()
    }

    private fun setupInfoText() {
        binding.infoTextLeftSwitcher.setup(gameViewModel, lifecycle)
        binding.infoTextRightSwitcher.setup(gameViewModel, lifecycle)
    }

    private fun setupMainButton() = binding.mainButton.setup(gameViewModel, lifecycle)

    private fun setSounds() {
        binding.colorButtonTopLeft.sound = SoundManager.chip1
        binding.colorButtonTopRight.sound = SoundManager.chip2
        binding.colorButtonBottomLeft.sound = SoundManager.chip3
        binding.colorButtonBottomRight.sound = SoundManager.chip4
    }

    private fun setListeners() = buttons.forEach(::setTouchListener)

    private fun setTouchListener(colorButton: ColorButton) = colorButton.setOnTouchListener { _, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                colorButton.pushed = true
                onPress(colorButton)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                colorButton.pushed = false
                if (nonePushed()) setNonePushed()
            }
        }
        true
    }

    private fun onPress(colorButton: ColorButton) {
        colorButton.playSound()
        gameViewModel.playerPushed.value = GameButton(buttons.indexOf(colorButton))
    }

    private fun nonePushed() = buttons.none { it.pushed }

    private fun setNonePushed() {
        gameViewModel.playerPushed.value = null
    }

    private fun observeColorSettings() = observe(settingsViewModel.colorSet) {
        binding.colorButtonTopLeft.setColorResource(it.colors.topLeft)
        binding.colorButtonTopRight.setColorResource(it.colors.topRight)
        binding.colorButtonBottomLeft.setColorResource(it.colors.bottomLeft)
        binding.colorButtonBottomRight.setColorResource(it.colors.bottomRight)
    }

    private fun observeButtonPlayBack() = observe(gameViewModel.buttonPlayBack) { gameButton ->
        when (gameButton) {
            null -> buttons.forEach { it.pushed = false }
            else -> buttons[gameButton.buttonNumber].pushed = true
        }
    }

    private fun observeGameState() = observe(gameViewModel.gameState) {
        when (it) {
            GameState.Watch -> disableButtons()
            else -> enableButtons()
        }
    }

    private fun enableButtons() = buttons.forEach { it.isEnabled = true }

    private fun disableButtons() = buttons.forEach { it.isEnabled = false }
}
