package hackman.trevor.copycat.ui.main

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import hackman.trevor.copycat.*
import hackman.trevor.copycat.system.getColor
import hackman.trevor.copycat.ui.settings.SettingsViewModel
import kotlinx.android.synthetic.main.color_grid.*
import kotlinx.android.synthetic.main.extra_buttons_landscape.*
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.title.*

class MainFragment : BaseFragment() {
    override val layout = R.layout.main_fragment

    private val sounds: SoundProvider by soundProvider()
    private val billing: BillingProvider by billingProvider()
    private val onBackPressed: OnBackPressed by onBackPressed()

    private val settingsViewModel: SettingsViewModel by viewModels()

    private var popInRan: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupColorButtons()
        setupExtraButtons()
        setupSettingsMenu()
        observeColorSettings()
        observeSettingsInBackground()
        observeSettingsHidden()
    }

    private fun setupColorButtons() {
        color_button_top_left.setup(sound = sounds.soundManager.chip1)
        color_button_top_right.setup(sound = sounds.soundManager.chip2)
        color_button_bottom_left.setup(sound = sounds.soundManager.chip3)
        color_button_bottom_right.setup(sound = sounds.soundManager.chip4)
    }

    private fun setupExtraButtons() =
        extra_buttons_layout.setup(sounds.soundManager, billing.billingManager, settingsViewModel)

    private fun setupSettingsMenu() =
        settings_menu.setup(settingsViewModel, viewLifecycleOwner, sounds.soundManager)

    private fun observeColorSettings() =
        observe(settingsViewModel.colorSet) {
            color_button_top_left.setup(colorInt = getColor(it.colorResources[0]))
            color_button_top_right.setup(colorInt = getColor(it.colorResources[1]))
            color_button_bottom_left.setup(colorInt = getColor(it.colorResources[2]))
            color_button_bottom_right.setup(colorInt = getColor(it.colorResources[3]))
        }

    private fun observeSettingsInBackground() =
        observe(settingsViewModel.inBackground) { inBackground ->
            onBackPressed.injectOnBackPressed {
                if (!inBackground) settingsViewModel.setInBackground(true)
                inBackground
            }
            setButtonsEnabled(inBackground)
        }

    private fun setButtonsEnabled(isEnabled: Boolean) {
        main_button.isEnabled = isEnabled
        more_games_button.isEnabled = isEnabled
        game_modes_button.isEnabled = isEnabled
        no_ads_button.isEnabled = isEnabled
        rate_app_button.isEnabled = isEnabled
        settings_button.isEnabled = isEnabled
    }

    private fun observeSettingsHidden() =
        observe(settingsViewModel.hidden) { hidden ->
            settings_menu.isVisible = !hidden
        }

    override fun onResume() {
        super.onResume()
        if (!popInRan) {
            main_title.popIn()
            popInRan = true
        }
    }
}
