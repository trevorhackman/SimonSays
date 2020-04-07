package hackman.trevor.copycat.ui.main

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import hackman.trevor.copycat.R
import hackman.trevor.copycat.system.Color
import hackman.trevor.copycat.system.displayMinimum
import hackman.trevor.copycat.system.dpToPixel
import hackman.trevor.copycat.system.getString
import kotlin.math.min

class GameModesButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatButton(context, attributeSet) {

    init {
        text = getString(R.string.game_modes_button)
        textSize = displayMinimum() * .028f
        setTextColor(Color.White)
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        if (width != 0 && height != 0) background = createBackground(width, height)
    }

    private fun createBackground(width: Int, height: Int): Drawable {
        val min = min(width, height)
        val radius = min * .4f
        val strokeWidth = dpToPixel(6)

        return GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(Color.Grey900)
            cornerRadius = radius
            setStroke(strokeWidth, Color.Black)
        }
    }
}
