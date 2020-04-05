package hackman.trevor.copycat.ui.main

import android.os.Bundle
import android.view.View
import hackman.trevor.copycat.BaseFragment
import hackman.trevor.copycat.R
import hackman.trevor.copycat.SoundProvider
import hackman.trevor.copycat.soundProvider
import kotlinx.android.synthetic.main.color_grid.*
import kotlinx.android.synthetic.main.title.*

class MainFragment : BaseFragment() {
    override val layout = R.layout.main_fragment

    private val sounds: SoundProvider by soundProvider()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupColorButtons()
    }

    override fun onResume() {
        super.onResume()
        main_title.popIn()
    }

    private fun setupColorButtons() {
        color_button_top_left.setup(sounds.sounds.chip1)
        color_button_top_right.setup(sounds.sounds.chip2)
        color_button_bottom_left.setup(sounds.sounds.chip3)
        color_button_bottom_right.setup(sounds.sounds.chip4)
    }
}
