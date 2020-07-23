package hackman.trevor.copycat.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import hackman.trevor.copycat.*
import hackman.trevor.copycat.logic.game.GamePlayer
import hackman.trevor.copycat.logic.game.GameState
import hackman.trevor.copycat.logic.viewmodels.*
import hackman.trevor.copycat.system.getString
import hackman.trevor.copycat.ui.FadeSpeed
import hackman.trevor.copycat.ui.fadeIn
import hackman.trevor.copycat.ui.fadeOut
import hackman.trevor.copycat.ui.game_modes.popupText
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.title.*

class MainFragment : BaseFragment() {
    override val layout = R.layout.main_fragment

    private val onBackPressed: OnBackPressed by onBackPressed()

    private val settingsViewModel: SettingsViewModel by viewModels<SettingsViewModelImpl>()
    private val gameModesViewModel: GameModesViewModel by viewModels<GameModesViewModelImpl>()
    private val gameViewModel: GameViewModel by viewModels<GameViewModelImpl>()

    private val gamePlayer by lazy {
        GamePlayer(gameViewModel, viewLifecycleOwner.lifecycle)
    }

    private var popInRan = false
    private var onMainMenu = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupColorButtons()
        setupExtraButtons()
        setupSettingsMenu()
        setupGameModesMenu()
        setupMainButton()
        setupInstructions()
        observeSettingsInBackground()
        observeGameModesInBackground()
        observeGameMode()
        observeGameState()
    }

    private fun setupColorButtons() =
        color_buttons.setup(settingsViewModel, gameViewModel, viewLifecycleOwner.lifecycle)

    private fun setupExtraButtons() = extra_buttons_layout.setup(settingsViewModel, gameModesViewModel)

    private fun setupSettingsMenu() = settings_menu.setup(settingsViewModel, viewLifecycleOwner.lifecycle)

    private fun setupGameModesMenu() = game_modes_menu.setup(gameModesViewModel, viewLifecycleOwner.lifecycle)

    private fun setupMainButton() = main_button.setup(gameViewModel, viewLifecycleOwner.lifecycle)

    private fun setupInstructions() = instructions.setup(lifecycleScope)

    private fun observeSettingsInBackground() = observe(settingsViewModel.inBackground) { inBackground ->
        onBackPressed.setBehavior {
            if (!inBackground) settingsViewModel.setInBackground(true)
            inBackground
        }
        fadeTitleAndExtraButtons(inBackground)
        fadeAdContainer(!inBackground)
    }

    private fun observeGameModesInBackground() = observe(gameModesViewModel.inBackground) { inBackground ->
        onBackPressed.setBehavior {
            if (!inBackground) gameModesViewModel.setInBackground(true)
            inBackground
        }
        fadeTitleAndExtraButtons(inBackground)
        fadeAdContainer(!inBackground)
    }

    private fun observeGameMode() = observe(gameModesViewModel.gameMode) {
        gameViewModel.setGameMode(it)
        instructions.text = getString(it.popupText())
    }

    private fun observeGameState() = observe(gameViewModel.gameState) {
        when (it) {
            GameState.MainMenu -> onMainMenu()
            else -> onGame()
        }
    }

    private fun onMainMenu() {
        onMainMenu = true
        fadeTitleAndExtraButtons(true)
        instructions.cancelAnimation()
        instructions.fadeOut()
    }

    private fun onGame() {
        if (onMainMenu) {
            onMainMenu = false
            fadeTitleAndExtraButtons(false, FadeSpeed.Slow)
            instructions.animateInstructions()
            gamePlayer.startGame()
        }
    }

    private fun fadeTitleAndExtraButtons(fadeIn: Boolean, speed: FadeSpeed = FadeSpeed.Default) {
        fade_top.pivotY = fade_top.y
        if (fadeIn) {
            main_title.fadeIn(speed)
            extra_buttons_layout.fadeIn(speed)
            fade_top.animate().setDuration(speed.fadeInDuration).scaleY(1f)
        } else {
            main_title.fadeOut(speed)
            extra_buttons_layout.fadeOut(speed)
            fade_top.animate().setDuration(speed.fadeOutDuration).scaleY(0.5f)
        }
    }

    private fun fadeAdContainer(fadeIn: Boolean) {
        if (fadeIn) {
            ad_container.fadeIn()
        } else {
            ad_container.fadeOut()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!popInRan) {
            main_title.popIn()
            popInRan = true
        }
    }
}
