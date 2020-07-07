package hackman.trevor.copycat.system

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import hackman.trevor.copycat.BaseFragment
import kotlin.math.min

//region Resource Getters

fun Fragment.getString(stringResource: StringResource) = getString(stringResource.string)

fun View.getString(stringResource: StringResource) = getString(stringResource.string)

fun View.getString(resId: Int) = context.getString(resId)

/** That's right, a goddamn star! Never seen this spread operator before? https://proandroiddev.com/kotlins-vararg-and-spread-operator-4200c07d65e1 */
fun View.getString(resId: Int, vararg args: Any) = context.getString(resId, *args)

fun View.getColor(colorResource: ColorResource) = getColor(colorResource.color)

fun View.getColor(resId: Int) = ContextCompat.getColor(context, resId)

fun View.getDrawable(resId: Int) = context.getDrawable(resId)

fun BaseFragment.getColor(colorResource: ColorResource) = getColor(colorResource.color)

private fun BaseFragment.getColor(resId: Int) = ContextCompat.getColor(requireContext(), resId)

//endregion

//region Display Metrics

fun View.displayWidth() = context.resources.displayMetrics.widthPixels

fun View.displayHeight() = context.resources.displayMetrics.heightPixels

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

//endregion

var TextView.pixelTextSize: Float
    get() = textSize
    set(value) = setTextSize(TypedValue.COMPLEX_UNIT_PX, value)

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
