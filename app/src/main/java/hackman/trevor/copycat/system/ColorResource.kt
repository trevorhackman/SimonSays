package hackman.trevor.copycat.system

import androidx.annotation.ColorRes

/** @param color is expected to be a color resource reference (e.g. android.R.color.black) */
data class ColorResource(@ColorRes val color: Int)
