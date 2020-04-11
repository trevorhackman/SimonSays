package hackman.trevor.copycat.ui.main

import android.os.Bundle
import android.view.View
import hackman.trevor.copycat.*
import kotlinx.android.synthetic.main.color_grid.*
import kotlinx.android.synthetic.main.main_buttons.*
import kotlinx.android.synthetic.main.title.*

class MainFragment : BaseFragment() {
    override val layout = R.layout.main_fragment

    private val sounds: SoundProvider by soundProvider()
    private val billing: BillingProvider by billingProvider()

    private var popInRan: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupColorButtons()
        no_ads_button.setup(billing.billing)
    }

    override fun onResume() {
        super.onResume()
        if (!popInRan) {
            main_title.popIn()
            popInRan = true
        }
    }

    private fun setupColorButtons() {
        color_button_top_left.setup(sounds.sounds.chip1)
        color_button_top_right.setup(sounds.sounds.chip2)
        color_button_bottom_left.setup(sounds.sounds.chip3)
        color_button_bottom_right.setup(sounds.sounds.chip4)
    }
}
