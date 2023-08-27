package hackman.trevor.copycat.ui.failure

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import hackman.trevor.copycat.databinding.FailureMenuBinding
import hackman.trevor.copycat.logic.viewmodels.FailureViewModel
import hackman.trevor.copycat.logic.viewmodels.GameViewModel
import hackman.trevor.copycat.observe
import hackman.trevor.copycat.system.displayHeight
import hackman.trevor.copycat.system.displayWidth
import hackman.trevor.copycat.ui.fadeOut
import hackman.trevor.copycat.ui.scroll_in_1000
import kotlin.math.min
import kotlin.math.sin

class FailureMenu @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : ConstraintLayout(context, attributeSet), LifecycleOwner {

    private lateinit var failureViewModel: FailureViewModel
    private lateinit var gameViewModel: GameViewModel
    override lateinit var lifecycle: Lifecycle

    private val binding = FailureMenuBinding.inflate(LayoutInflater.from(context), this, true)

    fun setup(failureViewModel: FailureViewModel, gameViewModel: GameViewModel, lifecycle: Lifecycle) {
        this.failureViewModel = failureViewModel
        this.gameViewModel = gameViewModel
        this.lifecycle = lifecycle
        setupBody()
        setupButtons()
        observeInBackground()
    }

    private fun setupBody() = binding.failureBody.setup(failureViewModel, lifecycle)

    private fun setupButtons() {
        binding.failureMainMenuButton.setup(gameViewModel)
        binding.failurePlayAgainButton.setup(gameViewModel)
    }

    private fun observeInBackground() = observe(failureViewModel.inBackground) {
        if (it) fadeOut()
        else animateIn()
    }

    private fun animateIn() {
        val screenHeight = displayHeight().toFloat()
        y = screenHeight
        isVisible = true
        alpha = 1f
        isEnabled = false
        animate()
            .y((screenHeight - height) / 2)
            .setDuration(scroll_in_1000)
            .setInterpolator(smoothMotionInterpolator)
            .withStartAction { failureViewModel.isAnimatingIn = true }
            .withEndAction {
                failureViewModel.isAnimatingIn = false
                isEnabled = true
            }
    }

    private val smoothMotionInterpolator = { x: Float ->
        sin(x * Math.PI / 2).toFloat()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.makeMeasureSpec(determineWidth(), MeasureSpec.EXACTLY)
        super.onMeasure(width, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        // If orientation change happens during scroll to center animation, position will change and animation must reset
        if (changed && failureViewModel.isAnimatingIn) animateIn()
    }

    private fun determineWidth() = min(displayWidth(), (displayWidth() + displayHeight()) / 2)

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        binding.failurePlayAgainButton.isEnabled = enabled
        binding.failureMainMenuButton.isEnabled = enabled
    }
}
