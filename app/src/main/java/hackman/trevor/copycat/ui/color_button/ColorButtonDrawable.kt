package hackman.trevor.copycat.ui.color_button

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import androidx.annotation.ColorInt
import hackman.trevor.copycat.system.Color
import hackman.trevor.copycat.system.brightenColor
import kotlin.math.max
import kotlin.math.min

class ColorButtonDrawable(
    width: Int,
    height: Int,
    @ColorInt private val buttonColor: Int
) {
    private val maxDimension = max(height, width)
    private val minDimension = min(height, width)
    private val cornerRadius = maxDimension / 15f

    fun normalGraphic(): Drawable =
        GradientDrawable().apply {
            setColor(buttonColor)
            cornerRadius = this@ColorButtonDrawable.cornerRadius
        }

    fun pressedGraphic(): Drawable =
        LayerDrawable(arrayOf(highlight(), shrunkButtonWithGlow()))

    private fun highlight(): Drawable =
        GradientDrawable().apply {
            applyCornerRadius()
            setColor(0xaaffffff.toInt())
            simulateShrinkage(minDimension * .145)
        }

    private fun shrunkButtonWithGlow(): Drawable =
        GradientDrawable().apply {
            applyCornerRadius()
            gradientType = GradientDrawable.RADIAL_GRADIENT
            gradientRadius = maxDimension / 2f
            colors = intArrayOf(brightenColor(buttonColor, .5), buttonColor)
            simulateShrinkage(minDimension * .155)
        }

    private fun GradientDrawable.applyCornerRadius() {
        cornerRadius = this@ColorButtonDrawable.cornerRadius
    }

    // Transparent stroke for a simulated shrinkage or 'press' effect
    private fun GradientDrawable.simulateShrinkage(width: Double) =
        setStroke(width.toInt(), Color.Transparent)
}
