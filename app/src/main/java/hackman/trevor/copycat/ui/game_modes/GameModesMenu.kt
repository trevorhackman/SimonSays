package hackman.trevor.copycat.ui.game_modes

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import hackman.trevor.copycat.databinding.GameModesMenuBinding
import hackman.trevor.copycat.logic.viewmodels.GameModesViewModel
import hackman.trevor.copycat.observe
import hackman.trevor.copycat.requireValue
import hackman.trevor.copycat.system.SaveData
import hackman.trevor.copycat.system.displayHeight
import hackman.trevor.copycat.system.displayWidth
import hackman.trevor.copycat.system.sound.SoundManager
import hackman.trevor.copycat.ui.fadeIn
import hackman.trevor.copycat.ui.fadeOut
import kotlin.math.min

class GameModesMenu @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : ConstraintLayout(context, attributeSet), LifecycleOwner {

    private lateinit var gameModesViewModel: GameModesViewModel
    override lateinit var lifecycle: Lifecycle

    private val binding = GameModesMenuBinding.inflate(LayoutInflater.from(context), this, true)

    fun setup(gameModesViewModel: GameModesViewModel, lifecycle: Lifecycle) {
        this.gameModesViewModel = gameModesViewModel
        this.lifecycle = lifecycle
        initGameMode()
        setupButtons()
        setupCloseButton()
        observeGameMode()
        observeInBackground()
    }

    private fun initGameMode() = gameModesViewModel.setGameMode(SaveData.gameMode)

    private fun setupButtons() = binding.gameModesButtons.setup(gameModesViewModel)

    private fun setupCloseButton() = binding.gameModesCloseButton.setOnClickListener {
        gameModesViewModel.setInBackground(true)
        SoundManager.click.play()
    }

    private fun observeGameMode() = observe(gameModesViewModel.gameMode) {
        binding.gameModesButtons.setSelectedMode(it)
        binding.gameModesDescription.updateText(it)
        binding.gameModesBestText.updateText(it)
    }

    private fun observeInBackground() = observe(gameModesViewModel.inBackground) {
        if (it) fadeOut()
        else fadeIn(startAction = {
            gameModesViewModel.isAnimatingIn = true
            binding.gameModesBestText.updateText(gameModesViewModel.gameMode.requireValue())
        }) {
            gameModesViewModel.isAnimatingIn = false
        }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        binding.gameModesCloseButton.isEnabled = enabled
        binding.gameModesButtons.isEnabled = enabled
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.makeMeasureSpec(determineWidth(), MeasureSpec.EXACTLY)
        super.onMeasure(width, heightMeasureSpec)
    }

    private fun determineWidth() = min(displayWidth(), (displayWidth() + displayHeight()) / 2)
}
