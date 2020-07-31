package hackman.trevor.copycat.ui.failure

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import hackman.trevor.copycat.R
import hackman.trevor.copycat.logic.game.name
import hackman.trevor.copycat.logic.viewmodels.FailureViewModel
import hackman.trevor.copycat.observe
import hackman.trevor.copycat.system.SaveData
import hackman.trevor.copycat.system.getString
import hackman.trevor.copycat.ui.game_modes.name
import kotlinx.android.synthetic.main.failure_body.view.*

class FailureBody @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : LinearLayout(context, attributeSet), LifecycleOwner {

    private lateinit var failureViewModel: FailureViewModel
    private lateinit var lifecycle: Lifecycle

    init {
        View.inflate(context, R.layout.failure_body, this)
    }

    fun setup(failureViewModel: FailureViewModel, lifecycle: Lifecycle) {
        this.failureViewModel = failureViewModel
        this.lifecycle = lifecycle
        observeMode()
        observeScore()
        observeBest()
        observePressed()
        observeCorrect()
    }

    private fun observeMode() = observe(failureViewModel.mode) {
        failure_value_mode.text = getString(it.name())
    }

    private fun observeScore() = observe(failureViewModel.score) {
        failure_value_score.text = it.score.toString()
    }

    private fun observeBest() = observe(failureViewModel.best) {
        failure_value_best.text = it.score.toString()
    }

    private fun observePressed() = observe(failureViewModel.pressed) {
        failure_value_pressed.text = getString(it.name(SaveData.colorSet))
    }

    private fun observeCorrect() = observe(failureViewModel.correct) {
        failure_value_correct.text = getString(it.name(SaveData.colorSet))
    }

    override fun getLifecycle() = lifecycle
}
