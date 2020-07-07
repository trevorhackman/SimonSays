package hackman.trevor.copycat.ui.main.main_button

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import hackman.trevor.copycat.R
import hackman.trevor.copycat.logic.game.GameMode
import hackman.trevor.copycat.logic.game.GameState
import hackman.trevor.copycat.logic.viewmodels.GameViewModel
import hackman.trevor.copycat.observe
import hackman.trevor.copycat.system.getString
import hackman.trevor.copycat.ui.fadeIn
import hackman.trevor.copycat.ui.fadeOut
import hackman.trevor.copycat.ui.game_modes.name
import hackman.trevor.copycat.ui.main.CircularTouchListener
import hackman.trevor.copycat.ui.plainFadeIn
import hackman.trevor.copycat.ui.plainFadeOut
import kotlinx.android.synthetic.main.main_button_frame_layout.view.*

class MainButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : FrameLayout(context, attributeSet), LifecycleOwner {

    private lateinit var gameViewModel: GameViewModel
    private lateinit var lifecycleOwner: LifecycleOwner

    init {
        View.inflate(context, R.layout.main_button_frame_layout, this)
    }

    fun setup(gameViewModel: GameViewModel, lifecycleOwner: LifecycleOwner) {
        this.gameViewModel = gameViewModel
        this.lifecycleOwner = lifecycleOwner
        setOnTouchListener(CircularTouchListener)
        observeGameMode()
        observeGameState()
        observeRoundNumber()
    }

    private fun observeGameMode() = observe(gameViewModel.gameMode) {
        main_button_game_mode_text.text = getString(it.name())
        main_button_game_mode_text.isVisible = it != GameMode.Classic
    }

    private fun observeGameState() = observe(gameViewModel.gameState) {
        when (it) {
            GameState.MainMenu -> onMainMenu()
            else -> onGame()
        }
    }

    private fun onMainMenu() {
        main_button_play_icon.fadeIn()
        main_button_play_icon.gyrate()
        main_button_game_mode_text.plainFadeIn()
        main_button_round_text.fadeOut()
        unshrink()
    }

    private fun onGame() {
        main_button_play_icon.fadeOut()
        main_button_game_mode_text.plainFadeOut()
        main_button_round_text.fadeIn()
        shrink()
    }

    private fun unshrink() = animate().setDuration(600).scaleX(1f).scaleY(1f)

    private fun shrink() = animate().setDuration(1000).scaleX(0.90f).scaleY(0.90f)

    private fun observeRoundNumber() = observe(gameViewModel.roundNumber) {
        main_button_round_text.text = it.toString()
    }

    override fun performClick(): Boolean {
        super.performClick()
        gameViewModel.playButtonClicked()
        return true
    }

    override fun getLifecycle(): Lifecycle = lifecycleOwner.lifecycle
}
