package hackman.trevor.copycat.system

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.view.View
import androidx.annotation.ColorInt
import kotlin.math.min

fun View.getString(resId: Int) = this.context.getString(resId)

fun View.getString(resId: Int, vararg args: Any) = this.context.getString(resId, args)

fun View.getDrawable(id: Int) = this.context.getDrawable(id)

fun View.displayWidth() = this.context.resources.displayMetrics.widthPixels

fun View.displayHeight() = this.context.resources.displayMetrics.heightPixels

fun View.displayMinimum() = min(displayWidth(), displayHeight())

fun View.isPortrait() = this.displayWidth() < this.displayHeight()

/**
 * This method converts dp unit to equivalent pixels, depending on device density
 *
 * @param dp A value in dp (density independent pixels) unit
 * @return A rounded int value representing pixel equivalent
 */
fun View.dpToPixel(dp: Double): Int = (dp * this.context.resources.displayMetrics.density).toInt()
fun View.dpToPixel(dp: Int): Int = dpToPixel(dp.toDouble())

/**
 * Create a ripple drawable for buttons - adds ripple effect onPress and onClick to a normal drawable
 * Ripple drawables can wrap around other kinds of drawables
 * @param background is not altered and is the appearance of the resulting RippleDrawable
 * @param pressedColor is the color of the ripple effect
 */
fun createRippleDrawable(background: Drawable?, @ColorInt pressedColor: Int): RippleDrawable {
    val colorStateList = ColorStateList(
        arrayOf(intArrayOf()), intArrayOf(
            pressedColor
        )
    )
    return RippleDrawable(colorStateList, background, null)
}
