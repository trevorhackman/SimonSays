package hackman.trevor.copycat.ui.failure

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import hackman.trevor.copycat.R
import hackman.trevor.copycat.logic.viewmodels.FailureViewModel
import hackman.trevor.copycat.logic.viewmodels.GameViewModel
import hackman.trevor.copycat.observe
import hackman.trevor.copycat.ui.fadeIn
import hackman.trevor.copycat.ui.fadeOut
import kotlinx.android.synthetic.main.failure_menu.view.*

class FailureMenu @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : ConstraintLayout(context, attributeSet), LifecycleOwner {

    private lateinit var failureViewModel: FailureViewModel
    private lateinit var gameViewModel: GameViewModel
    private lateinit var lifecycle: Lifecycle

    init {
        View.inflate(context, R.layout.failure_menu, this)
    }

    fun setup(failureViewModel: FailureViewModel, gameViewModel: GameViewModel, lifecycle: Lifecycle) {
        this.failureViewModel = failureViewModel
        this.gameViewModel = gameViewModel
        this.lifecycle = lifecycle
        setupButtons()
        observeInBackground()
    }

    private fun setupButtons() {
        failure_main_menu_button.setup(gameViewModel)
        failure_play_again_button.setup(gameViewModel)
    }

    private fun observeInBackground() = observe(failureViewModel.inBackground) {
        if (it) fadeOut()
        else fadeIn()
    }

    override fun getLifecycle(): Lifecycle = lifecycle
}
