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
import hackman.trevor.copycat.system.sound.SoundManager
import hackman.trevor.copycat.ui.*
import hackman.trevor.copycat.ui.game_modes.popupText
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.title.*

class MainFragment : BaseFragment() {
    override val layout = R.layout.main_fragment

    private val viewLifecycle by lazy {
        viewLifecycleOwner.lifecycle
    }

    private val onBackPressed: OnBackPressed by onBackPressed()

    private val settingsViewModel: SettingsViewModel by viewModels<SettingsViewModelImpl>()
    private val gameModesViewModel: GameModesViewModel by viewModels<GameModesViewModelImpl>()
    private val failureViewModel: FailureViewModel by viewModels<FailureViewModelImpl>()
    private val gameViewModel: GameViewModel by viewModels<GameViewModelImpl>()

    private val gamePlayer by lazy {
        GamePlayer(gameViewModel, failureViewModel, viewLifecycleOwner.lifecycle)
    }

    private var popInRan = false
    private var inGame = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupColorButtons()
        setupExtraButtons()
        setupSettingsMenu()
        setupGameModesMenu()
        setupFailureMenu()
        setupMainButton()
        setupInstructions()
        observeMenusInBackground()
        observeFailureMenu()
        observeGameMode()
        observeGameState()
    }

    private fun setupColorButtons() = color_buttons.setup(settingsViewModel, gameViewModel, viewLifecycle)

    private fun setupExtraButtons() = extra_buttons_layout.setup(settingsViewModel, gameModesViewModel)

    private fun setupSettingsMenu() = settings_menu.setup(settingsViewModel, viewLifecycle)

    private fun setupGameModesMenu() = game_modes_menu.setup(gameModesViewModel, viewLifecycle)

    private fun setupFailureMenu() = failure_menu.setup(failureViewModel, gameViewModel, viewLifecycle)

    private fun setupMainButton() = main_button.setup(gameViewModel, viewLifecycle)

    private fun setupInstructions() = instructions.setup(lifecycleScope)

    private fun observeMenusInBackground() = listOf(settingsViewModel, gameModesViewModel)
        .forEach(::observeMenu)

    private fun observeMenu(menu: Menu) = observe(menu.inBackground) { inBackground ->
        onBackPressed.setBehavior {
            if (menu.isAnimatingIn) BackEvent.Consumed
            else if (!inBackground) {
                menu.setInBackground(true)
                BackEvent.Consumed
            } else BackEvent.CallSuper
        }
        fadeTitleAndExtraButtons(inBackground)
        fadeAdContainer(!inBackground)
    }

    private fun observeFailureMenu() = observe(failureViewModel.inBackground) { inBackground ->
        onBackPressed.setBehavior {
            if (failureViewModel.isAnimatingIn) BackEvent.Consumed
            else if (!inBackground) {
                failureViewModel.setInBackground(true)
                gameViewModel.setGameState(GameState.MainMenu)
                BackEvent.Consumed
            }
            else BackEvent.CallSuper
        }
    }

    private fun observeGameMode() = observe(gameModesViewModel.gameMode) {
        gameViewModel.setGameMode(it)
        instructions.text = getString(it.popupText())
    }

    private fun observeGameState() = observe(gameViewModel.gameState) {
        if (it != GameState.Failure) failureViewModel.setInBackground(true)
        if (it != GameState.Watch && it != GameState.Input) inGame = false
        when (it) {
            GameState.MainMenu -> onMainMenu()
            GameState.Failure -> onFailure()
            else -> onGame()
        }
    }

    private fun onMainMenu() {
        fadeTitleAndExtraButtons(true)
        cancelInstructions()
    }

    private fun onFailure() {
        SoundManager.failure.play()
        failureViewModel.setInBackground(false)
        cancelInstructions()
    }

    // Cancel instructions in case they're still up
    private fun cancelInstructions() = instructions.apply {
        cancelAnimation()
        fadeOut()
    }

    private fun onGame() {
        if (!inGame) {
            inGame = true
            fadeTitleAndExtraButtons(false, outSpeed = fade_out_900)
            instructions.animateInstructions()
            gamePlayer.startGame()
            setInGameBackBehavior()
        }
    }

    private fun fadeTitleAndExtraButtons(fadeIn: Boolean, inSpeed: Long = fade_in_500, outSpeed: Long = fade_out_300) {
        fade_top.pivotY = fade_top.y
        if (fadeIn) {
            main_title.fadeIn().duration = inSpeed
            extra_buttons_layout.fadeIn().duration = inSpeed
            fade_top.animate().scaleY(1f).duration = inSpeed
        } else {
            main_title.fadeOut().duration = outSpeed
            extra_buttons_layout.fadeOut().duration = outSpeed
            fade_top.animate().scaleY(0.5f).duration = outSpeed
        }
    }

    private fun fadeAdContainer(fadeIn: Boolean) =
        if (fadeIn) ad_container.fadeIn()
        else ad_container.fadeOut()

    private fun setInGameBackBehavior() = onBackPressed.setBehavior {
        if (!justStartedGame()) {
            DialogFactory.leaveCurrentGame(
                onExit = { onBackPressed.callSuper() },
                onMainMenu = { gameViewModel.setGameState(GameState.MainMenu) }
            ).show()
        } else {
            gameViewModel.setGameState(GameState.MainMenu)
        }
        BackEvent.Consumed
    }

    // Not worth presenting dialog if just started game
    private fun justStartedGame() = gameViewModel.roundNumber.requireValue().roundNumber == 1

    override fun onResume() {
        super.onResume()
        if (!popInRan) {
            main_title.popIn()
            popInRan = true
        }
    }
}
