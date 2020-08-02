package hackman.trevor.copycat.ui

import android.view.View
import android.view.ViewPropertyAnimator
import androidx.core.view.isVisible

const val fade_in_900 = 900L
const val fade_in_500 = 500L
const val fade_out_300 = 300L
const val fade_out_900 = 900L

const val scroll_in_1000 = 1000L

fun View.plainFadeIn(): ViewPropertyAnimator = animate().alpha(1f).setDuration(fade_in_500)

fun View.plainFadeOut(): ViewPropertyAnimator = animate().alpha(0f).setDuration(fade_out_300)

inline fun View.fadeIn(
    crossinline startAction: () -> Unit = {},
    crossinline endAction: () -> Unit = {}
): ViewPropertyAnimator {
    return animate().alpha(1f)
        .setDuration(fade_in_500)
        .withStartAction {
            isVisible = true
            startAction()
        }
        .withEndAction {
            isEnabled = true
            endAction()
        }
}

inline fun View.fadeOut(
    crossinline startAction: () -> Unit = {},
    crossinline endAction: () -> Unit = {}
): ViewPropertyAnimator =
    animate().alpha(0f)
        .setDuration(fade_out_300)
        .withStartAction {
            isEnabled = false
            startAction()
        }
        .withEndAction {
            isVisible = false
            endAction()
        }
