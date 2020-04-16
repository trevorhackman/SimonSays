package hackman.trevor.copycat.ui

import android.view.View

private const val fadeInDuration = 500L
private const val fadeOutDuration = 300L

fun View.fadeIn(startAction: () -> Unit, endAction: () -> Unit) {
    animate().alpha(1f)
        .setDuration(fadeInDuration)
        .withStartAction(startAction)
        .withEndAction(endAction)
}

fun View.fadeOut(startAction: () -> Unit, endAction: () -> Unit) {
    animate().alpha(0f)
        .setDuration(fadeOutDuration)
        .withStartAction(startAction)
        .withEndAction(endAction)
}