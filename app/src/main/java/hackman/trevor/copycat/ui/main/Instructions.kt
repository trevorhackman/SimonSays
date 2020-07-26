package hackman.trevor.copycat.ui.main

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import hackman.trevor.copycat.R
import hackman.trevor.copycat.system.displayMinimum
import hackman.trevor.copycat.system.dpToPixel
import hackman.trevor.copycat.system.getDrawable
import hackman.trevor.copycat.system.pixelTextSize
import hackman.trevor.copycat.ui.FadeSpeed
import hackman.trevor.copycat.ui.fadeIn
import hackman.trevor.copycat.ui.fadeOut
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Instructions @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatTextView(context, attributeSet) {

    private lateinit var lifecycleScope: CoroutineScope

    private var instructionsFadeJob: Job? = null

    init {
        background = getDrawable(R.drawable.instructions_rectangle)
        pixelTextSize = displayMinimum() * .054f
        setPadding(dpToPixel(8))
        isVisible = false
        alpha = 0f
    }

    fun setup(lifecycleScope: CoroutineScope) {
        this.lifecycleScope = lifecycleScope
    }

    fun cancelAnimation() {
        instructionsFadeJob?.cancel()
        resetOnEnd()
    }

    fun animateInstructions() {
        instructionsFadeJob = lifecycleScope.launch {
            delay(600)
            fadeIn(FadeSpeed.Default)
            delay(3000)
            fadeOut(FadeSpeed.Slow) {
                resetOnEnd()
            }.scaleY(0.25f).translationY(dpToPixel(75).toFloat())
        }
    }

    private fun resetOnEnd() {
        scaleY = 1f
        translationY = 0f
    }
}
