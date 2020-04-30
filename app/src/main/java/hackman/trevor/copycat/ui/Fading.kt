package hackman.trevor.copycat.ui

import android.view.View
import android.view.ViewPropertyAnimator
import androidx.core.view.isVisible

private const val fadeInDuration = 500L
private const val fadeOutDuration = 300L

fun View.fadeIn(startAction: () -> Unit = {}, endAction: () -> Unit = {}): ViewPropertyAnimator =
    animate().alpha(1f)
        .setDuration(fadeInDuration)
        .withStartAction {
            isVisible = true
            startAction()
        }
        .withEndAction {
            isEnabled = true
            endAction()
        }

fun View.fadeOut(startAction: () -> Unit = {}, endAction: () -> Unit = {}): ViewPropertyAnimator =
    animate().alpha(0f)
        .setDuration(fadeOutDuration)
        .withStartAction {
            isEnabled = false
            startAction()
        }
        .withEndAction {
            isVisible = false
            endAction()
        }
