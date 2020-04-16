package hackman.trevor.copycat.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import hackman.trevor.copycat.*
import hackman.trevor.copycat.system.getColor
import hackman.trevor.copycat.ui.settings.SettingsViewModel
import kotlinx.android.synthetic.main.color_grid.*
import kotlinx.android.synthetic.main.main_buttons.*
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.title.*

class MainFragment : BaseFragment() {
    override val layout = R.layout.main_fragment

    private val sounds: SoundProvider by soundProvider()
    private val billing: BillingProvider by billingProvider()

    private val settingsViewModel: SettingsViewModel by viewModels()

    private var popInRan: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupColorButtons()
        setupNoAdsButton()
        setupSettingsButton()
        setupSettingsMenu()
        observeColorSettings()
    }

    private fun setupColorButtons() {
        color_button_top_left.setup(sound = sounds.sounds.chip1)
        color_button_top_right.setup(sound = sounds.sounds.chip2)
        color_button_bottom_left.setup(sound = sounds.sounds.chip3)
        color_button_bottom_right.setup(sound = sounds.sounds.chip4)
    }

    private fun setupNoAdsButton() = no_ads_button.setup(billing.billing)

    private fun setupSettingsButton() = settings_button.setup(settingsViewModel)

    private fun setupSettingsMenu() = settings_menu.setup(settingsViewModel, viewLifecycleOwner, sounds.sounds)

    private fun observeColorSettings() =
        observe(settingsViewModel.colorSet) {
            color_button_top_left.setup(colorInt = getColor(it.colorResources[0]))
            color_button_top_right.setup(colorInt = getColor(it.colorResources[1]))
            color_button_bottom_left.setup(colorInt = getColor(it.colorResources[2]))
            color_button_bottom_right.setup(colorInt = getColor(it.colorResources[3]))
        }

    override fun onResume() {
        super.onResume()
        if (!popInRan) {
            main_title.popIn()
            popInRan = true
        }
    }
}
