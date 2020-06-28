package hackman.trevor.copycat.ui.main

import android.view.MotionEvent
import android.view.View

/**
 * Gives circular touch listener
 * Assumes view is square in dimensions
 *
 * Returning true causes the view to consume the event
 * Returning false causes the view to pass the event, allowing others to potentially consume
 */
val CircularTouchListener = View.OnTouchListener { view, event ->
    val xFromTopLeft = event.x.toDouble()
    val yFromTopLeft = event.y.toDouble()

    val xFromCenter = xFromTopLeft - view.width / 2.0
    val yFromCenter = yFromTopLeft - view.height / 2.0

    val radius = view.width / 2

    if (xFromCenter * xFromCenter + yFromCenter * yFromCenter <= radius * radius) {
        if (event.action == MotionEvent.ACTION_UP) {
            view.performClick()
        }
        true
    }
    else false
}
