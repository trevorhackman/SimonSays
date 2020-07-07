package hackman.trevor.copycat.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import hackman.trevor.copycat.*
import hackman.trevor.copycat.logic.game.GameState
import hackman.trevor.copycat.logic.viewmodels.*
import hackman.trevor.copycat.system.getString
import hackman.trevor.copycat.system.sound.SoundManager
import hackman.trevor.copycat.ui.FadeSpeed
import hackman.trevor.copycat.ui.fadeIn
import hackman.trevor.copycat.ui.fadeOut
import hackman.trevor.copycat.ui.game_modes.popupText
import kotlinx.android.synthetic.main.color_grid.*
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.title.*
import kotlinx.coroutines.*

class MainFragment : BaseFragment() {
    override val layout = R.layout.main_fragment

    private val onBackPressed: OnBackPressed by onBackPressed()

    private val settingsViewModel: SettingsViewModel by viewModels<SettingsViewModelImpl>()
    private val gameModesViewModel: GameModesViewModel by viewModels<GameModesViewModelImpl>()
    private val gameViewModel: GameViewModel by viewModels<GameViewModelImpl>()

    private var popInRan: Boolean = false

    private var instructionsFadeJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupColorButtons()
        setupExtraButtons()
        setupSettingsMenu()
        setupGameModesMenu()
        setupMainButton()
        observeColorSettings()
        observeSettingsInBackground()
        observeGameModesInBackground()
        observeGameMode()
        observeGameState()
    }

    private fun setupColorButtons() {
        color_button_top_left.setup(sound = SoundManager.chip1)
        color_button_top_right.setup(sound = SoundManager.chip2)
        color_button_bottom_left.setup(sound = SoundManager.chip3)
        color_button_bottom_right.setup(sound = SoundManager.chip4)
    }

    private fun setupExtraButtons() = extra_buttons_layout.setup(settingsViewModel, gameModesViewModel)

    private fun setupSettingsMenu() = settings_menu.setup(settingsViewModel, viewLifecycleOwner)

    private fun setupGameModesMenu() = game_modes_menu.setup(gameModesViewModel, viewLifecycleOwner)

    private fun setupMainButton() = main_button.setup(gameViewModel, viewLifecycleOwner)

    private fun observeColorSettings() = observe(settingsViewModel.colorSet) {
        color_button_top_left.setup(colorResource = it.colors.topLeft)
        color_button_top_right.setup(colorResource = it.colors.topRight)
        color_button_bottom_left.setup(colorResource = it.colors.bottomLeft)
        color_button_bottom_right.setup(colorResource = it.colors.bottomRight)
    }

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
        fadeTitleAndExtraButtons(true)
        instructions.fadeOut()
    }

    private fun onGame() {
        fadeTitleAndExtraButtons(false, FadeSpeed.Slow)
        fadeInstructions()
    }

    // TODO Move to instructions class
    private fun fadeInstructions() {
        instructionsFadeJob?.cancel()
        instructionsFadeJob = lifecycleScope.launch {
            withContext(Dispatchers.Default) { delay(600) }
            instructions.fadeIn(FadeSpeed.Default)
            withContext(Dispatchers.Default) { delay(3000) }
            instructions.fadeOut(FadeSpeed.Slow)
        }
    }

    private fun fadeTitleAndExtraButtons(fadeIn: Boolean, speed: FadeSpeed = FadeSpeed.Default) {
        if (fadeIn) {
            main_title.fadeIn(speed)
            extra_buttons_layout.fadeIn(speed)
        } else {
            main_title.fadeOut(speed)
            extra_buttons_layout.fadeOut(speed)
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
