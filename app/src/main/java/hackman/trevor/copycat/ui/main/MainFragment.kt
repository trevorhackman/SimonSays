package hackman.trevor.copycat.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import hackman.trevor.copycat.BaseFragment
import hackman.trevor.copycat.InjectorViewModel
import hackman.trevor.copycat.R
import hackman.trevor.copycat.SoundProvider
import kotlinx.android.synthetic.main.color_grid.*

class MainFragment : BaseFragment() {
    override val layout = R.layout.main_fragment

    private val sounds: SoundProvider by activityViewModels<InjectorViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupColorButtons()
    }

    private fun setupColorButtons() {
        color_button_top_left.setup(sounds.sounds.chip1)
        color_button_top_right.setup(sounds.sounds.chip2)
        color_button_bottom_left.setup(sounds.sounds.chip3)
        color_button_bottom_right.setup(sounds.sounds.chip4)
    }
}
