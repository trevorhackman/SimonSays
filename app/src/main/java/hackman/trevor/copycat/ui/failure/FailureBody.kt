package hackman.trevor.copycat.ui.failure

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import hackman.trevor.copycat.databinding.FailureBodyBinding
import hackman.trevor.copycat.logic.game.name
import hackman.trevor.copycat.logic.viewmodels.FailureViewModel
import hackman.trevor.copycat.observe
import hackman.trevor.copycat.system.SaveData
import hackman.trevor.copycat.system.getString

class FailureBody @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : FrameLayout(context, attributeSet), LifecycleOwner {

    private lateinit var failureViewModel: FailureViewModel
    override lateinit var lifecycle: Lifecycle

    private val binding = FailureBodyBinding.inflate(LayoutInflater.from(context), this, true)

    fun setup(failureViewModel: FailureViewModel, lifecycle: Lifecycle) {
        this.failureViewModel = failureViewModel
        this.lifecycle = lifecycle
        observeTopField()
        observeTopValue()
        observeScore()
        observeBest()
        observePressed()
        observeCorrect()
    }

    private fun observeTopField() = observe(failureViewModel.topTextField) {
        binding.failureFieldTop.text = getString(it)
    }

    private fun observeTopValue() = observe(failureViewModel.topTextValue) {
        binding.failureValueTop.text = getString(it)
    }

    private fun observeScore() = observe(failureViewModel.score) {
        binding.failureValueScore.text = it.score.toString()
    }

    private fun observeBest() = observe(failureViewModel.best) {
        binding.failureValueBest.text = it.score.toString()
    }

    private fun observePressed() = observe(failureViewModel.pressed) {
        binding.failureValuePressed.text = getString(it.name(SaveData.colorSet))
    }

    private fun observeCorrect() = observe(failureViewModel.correct) {
        binding.failureValueCorrect.text = getString(it.name(SaveData.colorSet))
    }
}
