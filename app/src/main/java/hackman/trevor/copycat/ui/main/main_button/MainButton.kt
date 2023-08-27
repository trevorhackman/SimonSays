package hackman.trevor.copycat.ui.main.main_button

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import hackman.trevor.copycat.databinding.MainButtonFrameLayoutBinding
import hackman.trevor.copycat.logic.game.GameMode
import hackman.trevor.copycat.logic.game.GameState
import hackman.trevor.copycat.logic.viewmodels.GameViewModel
import hackman.trevor.copycat.observe
import hackman.trevor.copycat.system.getString
import hackman.trevor.copycat.ui.fadeIn
import hackman.trevor.copycat.ui.fadeOut
import hackman.trevor.copycat.ui.fade_in_900
import hackman.trevor.copycat.ui.fade_out_900
import hackman.trevor.copycat.ui.game_modes.name
import hackman.trevor.copycat.ui.main.CircularTouchListener
import hackman.trevor.copycat.ui.plainFadeIn
import hackman.trevor.copycat.ui.plainFadeOut

class MainButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : FrameLayout(context, attributeSet), LifecycleOwner {

    private lateinit var gameViewModel: GameViewModel
    override lateinit var lifecycle: Lifecycle

    private val binding = MainButtonFrameLayoutBinding.inflate(LayoutInflater.from(context), this, true)

    fun setup(gameViewModel: GameViewModel, lifecycle: Lifecycle) {
        this.gameViewModel = gameViewModel
        this.lifecycle = lifecycle
        setOnTouchListener(CircularTouchListener)
        observeGameMode()
        observeGameState()
        observeRoundNumber()
    }

    private fun observeGameMode() = observe(gameViewModel.gameMode) {
        binding.mainButtonGameModeText.text = getString(it.name())
        binding.mainButtonGameModeText.isVisible = it != GameMode.Normal
    }

    private fun observeGameState() = observe(gameViewModel.gameState) {
        when (it) {
            GameState.MainMenu -> onMainMenu()
            else -> onGame()
        }
    }

    private fun onMainMenu() {
        binding.mainButtonPlayIcon.fadeIn {
            binding.mainButtonPlayIcon.gyrate()
        }.scaleX(1f).scaleY(1f).duration = fade_in_900
        binding.mainButtonGameModeText.plainFadeIn()
        binding.mainButtonRoundText.isVisible = false
        unshrink()
    }

    private fun onGame() {
        binding.mainButtonPlayIcon.fadeOut().scaleX(0.45f).scaleY(0.45f).duration = fade_out_900
        binding.mainButtonGameModeText.plainFadeOut()
        binding.mainButtonRoundText.fadeIn()
        shrink()
    }

    private fun unshrink() = animate().setDuration(600).scaleX(1f).scaleY(1f)

    private fun shrink() = animate().setDuration(1000).scaleX(0.90f).scaleY(0.90f)

    private fun observeRoundNumber() = observe(gameViewModel.roundNumber) {
        binding.mainButtonRoundText.text = it.roundNumber.toString()
    }

    override fun performClick(): Boolean {
        if (isMainMenu()) startGame()
        return true
    }

    private fun isMainMenu() = gameViewModel.gameState.value == GameState.MainMenu

    private fun startGame() {
        gameViewModel.gameState.value = GameState.Watch
    }
}
