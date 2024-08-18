package hackman.trevor.copycat.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import hackman.trevor.billing.Billing
import hackman.trevor.billing.BillingResponse.BILLING_UNAVAILABLE
import hackman.trevor.billing.BillingResponse.NETWORK_ERROR
import hackman.trevor.billing.BillingResponse.SUCCESSFUL_PURCHASE
import hackman.trevor.billing.BillingResponse.UNKNOWN_ERROR
import hackman.trevor.copycat.BackEvent
import hackman.trevor.copycat.Orientation
import hackman.trevor.copycat.ViewBindingFragment
import hackman.trevor.copycat.databinding.MainFragmentBinding
import hackman.trevor.copycat.databinding.TitleBinding
import hackman.trevor.copycat.fragmentInterface
import hackman.trevor.copycat.logic.game.GamePlayer
import hackman.trevor.copycat.logic.game.GameState
import hackman.trevor.copycat.logic.remove_ads.Prices
import hackman.trevor.copycat.logic.settings.toSound
import hackman.trevor.copycat.logic.viewmodels.FailureViewModel
import hackman.trevor.copycat.logic.viewmodels.FailureViewModelImpl
import hackman.trevor.copycat.logic.viewmodels.GameModesViewModel
import hackman.trevor.copycat.logic.viewmodels.GameModesViewModelImpl
import hackman.trevor.copycat.logic.viewmodels.GameViewModel
import hackman.trevor.copycat.logic.viewmodels.GameViewModelImpl
import hackman.trevor.copycat.logic.viewmodels.Menu
import hackman.trevor.copycat.logic.viewmodels.RemoveAdsViewModel
import hackman.trevor.copycat.logic.viewmodels.RemoveAdsViewModelImpl
import hackman.trevor.copycat.logic.viewmodels.SettingsViewModel
import hackman.trevor.copycat.logic.viewmodels.SettingsViewModelImpl
import hackman.trevor.copycat.observe
import hackman.trevor.copycat.requireValue
import hackman.trevor.copycat.system.SaveData
import hackman.trevor.copycat.system.ads.AdManager
import hackman.trevor.copycat.system.getString
import hackman.trevor.copycat.ui.DialogFactory
import hackman.trevor.copycat.ui.fadeIn
import hackman.trevor.copycat.ui.fadeOut
import hackman.trevor.copycat.ui.fade_in_500
import hackman.trevor.copycat.ui.fade_out_300
import hackman.trevor.copycat.ui.fade_out_900
import hackman.trevor.copycat.ui.game_modes.popupText
import hackman.trevor.copycat.ui.showCorrectly

class MainFragment : ViewBindingFragment<MainFragmentBinding>(MainFragmentBinding::inflate) {

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

    private val leaveGameDialog by lazy {
        DialogFactory.leaveCurrentGame(
            onExit = { fragmentInterface.callSuper() },
            onMainMenu = { gameViewModel.gameState.value = GameState.MainMenu }
        )
    }

    private var popInRan = false
    private var inGame = false

    private var _includedTitleBinding: TitleBinding? = null
    private val includedTitleBinding get() = _includedTitleBinding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _includedTitleBinding = TitleBinding.bind(binding.root)

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
            includedTitleBinding.mainTitle.popIn()
            popInRan = true
        }
    }

    private fun setupColorButtons() = binding.colorButtons.setup(settingsViewModel, gameViewModel, viewLifecycle)

    private fun setupExtraButtons() = binding.extraButtonsLayout.setup(
        settingsViewModel,
        gameModesViewModel,
        removeAdsViewModel
    )

    private fun setupSettingsMenu() = binding.settingsMenu.setup(settingsViewModel, viewLifecycle)

    private fun setupGameModesMenu() = binding.gameModesMenu.setup(gameModesViewModel, viewLifecycle)

    private fun setupFailureMenu() = binding.failureMenu.setup(failureViewModel, gameViewModel, viewLifecycle)

    private fun setupRemoveAdsMenu() = binding.removeAdsMenu.setup(removeAdsViewModel, viewLifecycle)

    private fun setupInstructions() = binding.instructions.setup(lifecycleScope)

    private fun setupAdContainer() = binding.adContainer.setup(viewLifecycle)

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

    private fun observeMenusInBackground() =
        listOf(settingsViewModel, gameModesViewModel, removeAdsViewModel).forEach(::observeMenu)

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
        binding.instructions.text = getString(it.popupText())
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
        if (AdManager.IS_ENABLED && !wouldBeBadInitialExperience()) AdManager // AdManager.showInterstitialAd(0.30)
    }

    // Would be bad initial experience to show ad on first several games
    private fun wouldBeBadInitialExperience() = SaveData.gamesCompleted < 6

    // Cancel instructions in case they're still up
    private fun cancelInstructions() = binding.instructions.apply {
        cancelAnimation()
        fadeOut()
    }

    private fun onGame() {
        if (!inGame) {
            inGame = true
            fadeTitleAndExtraButtons(false, outSpeed = fade_out_900)
            binding.instructions.animateInstructions()
            gamePlayer.startGame()
            setInGameBackBehavior()
        }
    }

    private fun fadeTitleAndExtraButtons(fadeIn: Boolean, inSpeed: Long = fade_in_500, outSpeed: Long = fade_out_300) {
        binding.fadeTop.pivotY = binding.fadeTop.y
        if (fadeIn) {
            includedTitleBinding.mainTitle.fadeIn().duration = inSpeed
            binding.extraButtonsLayout.animate { fadeIn().setDuration(inSpeed) }
            binding.fadeTop.animate().scaleY(1f).duration = inSpeed
        } else {
            includedTitleBinding.mainTitle.fadeOut().duration = outSpeed
            binding.extraButtonsLayout.animate { fadeOut().setDuration(outSpeed) }
            binding.fadeTop.animate().scaleY(0.5f).duration = outSpeed
        }
    }

    private fun fadeAdContainer(fadeIn: Boolean) =
        if (fadeIn) binding.adContainer.fadeIn()
        else binding.adContainer.fadeOut()

    private fun setInGameBackBehavior() = fragmentInterface.setBackBehavior {
        if (inLongGame()) {
            leaveGameDialog.showCorrectly()
            BackEvent.Consumed
        } else if (gameViewModel.gameState.value != GameState.MainMenu) {
            gameViewModel.gameState.value = GameState.MainMenu
            BackEvent.Consumed
        } else {
            BackEvent.CallSuper
        }
    }

    // Not worth presenting dialog if just started game
    private fun inLongGame() =
            gameViewModel.roundNumber.requireValue().roundNumber > 1 &&
            gameViewModel.gameState.requireValue().inGame

    private var isInitial = true

    private fun observeFailureSetting() = observe(settingsViewModel.failureSound) {
        if (isInitial) {
            isInitial = false
            return@observe
        }
        it.toSound().play()
    }
}
