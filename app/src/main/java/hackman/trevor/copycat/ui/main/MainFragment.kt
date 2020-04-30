package hackman.trevor.copycat.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import hackman.trevor.copycat.*
import hackman.trevor.copycat.system.getColor
import hackman.trevor.copycat.ui.fadeIn
import hackman.trevor.copycat.ui.fadeOut
import hackman.trevor.copycat.ui.settings.SettingsViewModel
import kotlinx.android.synthetic.main.color_grid.*
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.title.*

class MainFragment : BaseFragment() {
    override val layout = R.layout.main_fragment

    private val sounds: SoundProvider by soundProvider()
    private val billing: BillingProvider by billingProvider()
    private val ads: AdProvider by adProvider()
    private val onBackPressed: OnBackPressed by onBackPressed()

    private val settingsViewModel: SettingsViewModel by viewModels()

    private var popInRan: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupColorButtons()
        setupExtraButtons()
        setupSettingsMenu()
        setupAdContainer()
        observeColorSettings()
        observeSettingsInBackground()
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

    private fun setupAdContainer() =
        ad_container.setup(ads.adManager)

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
            fadeItems(inBackground)
        }

    private fun fadeItems(fadeIn: Boolean) {
        if (fadeIn) {
            main_title.fadeIn()
            extra_buttons_layout.fadeIn()
            ad_container.fadeOut()
        } else {
            main_title.fadeOut()
            extra_buttons_layout.fadeOut()
            ad_container.fadeIn()
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
