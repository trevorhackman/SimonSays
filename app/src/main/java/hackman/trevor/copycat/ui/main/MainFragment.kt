package hackman.trevor.copycat.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import hackman.trevor.billing.Billing
import hackman.trevor.billing.BillingResponse.*
import hackman.trevor.copycat.*
import hackman.trevor.copycat.logic.game.GamePlayer
import hackman.trevor.copycat.logic.game.GameState
import hackman.trevor.copycat.logic.remove_ads.Prices
import hackman.trevor.copycat.logic.settings.toSound
import hackman.trevor.copycat.logic.viewmodels.*
import hackman.trevor.copycat.system.SaveData
import hackman.trevor.copycat.system.ads.AdManager
import hackman.trevor.copycat.system.getString
import hackman.trevor.copycat.ui.*
import hackman.trevor.copycat.ui.game_modes.popupText
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.title.*

class MainFragment : BaseFragment() {
    override val layout = R.layout.main_fragment

    private val viewLifecycle by lazy {
        viewLifecycleOwner.lifecycle
    }

    private val fragmentInterface by fragmentInterface()

    private val failureViewModel: FailureViewModel by viewModels<FailureViewModelImpl>()
    private val gameModesViewModel: GameModesViewModel by viewModels<GameModesViewModelImpl>()
    private val gameViewModel: GameViewModel by viewModels<GameViewModelImpl>()
    private val removeAdsViewModel: RemoveAdsViewModel by viewModels<RemoveAdsViewModelImpl>()
    private val settingsViewModel: SettingsViewModel by viewModels<SettingsViewModelImpl>()

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
        setupRemoveAdsMenu()
        setupInstructions()
        setupAdContainer()
        observeBillingSkuDetails()
        observeBillingRetrySku()
        observeBillingResponse()
        observeMenusInBackground()
        observeFailureMenu()
        observeGameMode()
        observeGameState()
        observeFailureSetting()
    }

    override fun onResume() {
        super.onResume()
        if (!popInRan) {
            main_title.popIn()
            popInRan = true
        }
    }

    private fun setupColorButtons() = color_buttons.setup(settingsViewModel, gameViewModel, viewLifecycle)

    private fun setupExtraButtons() = extra_buttons_layout.setup(
        settingsViewModel,
        gameModesViewModel,
        removeAdsViewModel
    )

    private fun setupSettingsMenu() = settings_menu.setup(settingsViewModel, viewLifecycle)

    private fun setupGameModesMenu() = game_modes_menu.setup(gameModesViewModel, viewLifecycle)

    private fun setupFailureMenu() = failure_menu.setup(failureViewModel, gameViewModel, viewLifecycle)

    private fun setupRemoveAdsMenu() = remove_ads_menu.setup(removeAdsViewModel, viewLifecycle)

    private fun setupInstructions() = instructions.setup(lifecycleScope)

    private fun setupAdContainer() = ad_container.setup(viewLifecycle)

    private fun observeBillingSkuDetails() = observe(Billing.liveData.skuDetails) {
        removeAdsViewModel.prices.value = Prices(it[0].price, it[1].price, it[2].price, it[3].price)
    }

    private fun observeBillingRetrySku() = observe(Billing.liveData.retrySkuRetrievalSuccess) {
        when {
            gameViewModel.gameState.requireValue().inGame -> return@observe
            it -> removeAdsViewModel.setInBackground(false)
            else -> DialogFactory.failedNetwork().showCorrectly()
        }
    }

    private fun observeBillingResponse() = observe(Billing.liveData.billingResponse) {
        when (it) {
            SUCCESSFUL_PURCHASE -> DialogFactory.successfulNoAdsPurchase().showCorrectly()
            BILLING_UNAVAILABLE -> DialogFactory.billingUnavailable().showCorrectly()
            NETWORK_ERROR -> DialogFactory.failedNetwork().showCorrectly()
            UNKNOWN_ERROR -> DialogFactory.unknownError(it.errorMessage).showCorrectly()
        }
    }

    private fun observeMenusInBackground() = listOf(settingsViewModel, gameModesViewModel, removeAdsViewModel).forEach(::observeMenu)

    private fun observeMenu(menu: Menu) = observe(menu.inBackground) { inBackground ->
        fragmentInterface.setBackBehavior {
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
        fragmentInterface.setBackBehavior {
            if (failureViewModel.isAnimatingIn) BackEvent.Consumed
            else if (!inBackground) {
                failureViewModel.setInBackground(true)
                gameViewModel.gameState.value = GameState.MainMenu
                BackEvent.Consumed
            } else BackEvent.CallSuper
        }
    }

    private fun observeGameMode() = observe(gameModesViewModel.gameMode) {
        gameViewModel.gameMode.value = it
        instructions.text = getString(it.popupText())
    }

    private fun observeGameState() = observe(gameViewModel.gameState) {
        if (it != GameState.Failure) failureViewModel.setInBackground(true)
        if (!it.inGame) inGame = false
        when (it) {
            GameState.MainMenu -> onMainMenu()
            GameState.Failure -> onFailure()
            else -> onGame()
        }
        fragmentInterface.setOrientation(if (it.inGame) Orientation.Locked else Orientation.User)
    }

    private fun onMainMenu() {
        fadeTitleAndExtraButtons(true)
        cancelInstructions()
    }

    private fun onFailure() {
        failureViewModel.setInBackground(false)
        cancelInstructions()
        SaveData.gamesCompleted++
        if (AdManager.isEnabled && !wouldBeBadInitialExperience()) AdManager.showInterstitialAd(0.40)
    }

    // Would be bad initial experience to show ad on first couple games
    private fun wouldBeBadInitialExperience() = SaveData.gamesCompleted < 4

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

    private fun setInGameBackBehavior() = fragmentInterface.setBackBehavior {
        if (!justStartedGame()) {
            DialogFactory.leaveCurrentGame(
                onExit = { fragmentInterface.callSuper() },
                onMainMenu = { gameViewModel.gameState.value = GameState.MainMenu }
            ).showCorrectly()
            BackEvent.Consumed
        } else if (gameViewModel.gameState.value != GameState.MainMenu) {
            gameViewModel.gameState.value = GameState.MainMenu
            BackEvent.Consumed
        } else {
            BackEvent.CallSuper
        }
    }

    // Not worth presenting dialog if just started game
    private fun justStartedGame() = gameViewModel.roundNumber.requireValue().roundNumber == 1

    private var isInitial = true

    private fun observeFailureSetting() = observe(settingsViewModel.failureSound) {
        if (isInitial) {
            isInitial = false
            return@observe
        }
        it.toSound().play()
    }
}
