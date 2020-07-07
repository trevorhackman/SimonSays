package hackman.trevor.copycat.ui

import android.view.View
import android.view.ViewPropertyAnimator
import androidx.core.view.isVisible

sealed class FadeSpeed(val fadeInDuration: Long, val fadeOutDuration: Long) {
    object Default : FadeSpeed(500L, 300L)
    object Slow : FadeSpeed(1500L, 900L)
}

fun View.plainFadeIn(): ViewPropertyAnimator = animate().alpha(1f).setDuration(FadeSpeed.Default.fadeInDuration)

fun View.plainFadeOut(): ViewPropertyAnimator = animate().alpha(0f).setDuration(FadeSpeed.Default.fadeOutDuration)

fun View.fadeIn(
    speed: FadeSpeed = FadeSpeed.Default,
    startAction: () -> Unit = {},
    endAction: () -> Unit = {}
): ViewPropertyAnimator =
    animate().alpha(1f)
        .setDuration(speed.fadeInDuration)
        .withStartAction {
            isVisible = true
            startAction()
        }
        .withEndAction {
            isEnabled = true
            endAction()
        }

fun View.fadeOut(
    speed: FadeSpeed = FadeSpeed.Default,
    startAction: () -> Unit = {},
    endAction: () -> Unit = {}
): ViewPropertyAnimator =
    animate().alpha(0f)
        .setDuration(speed.fadeOutDuration)
        .withStartAction {
            isEnabled = false
            startAction()
        }
        .withEndAction {
            isVisible = false
            endAction()
        }
