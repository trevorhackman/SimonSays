package hackman.trevor.copycat.system

import android.view.View
import kotlin.math.min

fun View.displayWidth() = this.context.resources.displayMetrics.widthPixels

fun View.displayHeight() = this.context.resources.displayMetrics.heightPixels

fun View.displayMinimum() = min(displayWidth(), displayHeight())

fun View.isPortrait() = this.displayWidth() < this.displayHeight()
