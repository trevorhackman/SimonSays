package hackman.trevor.copycat.ui.color_button

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageButton
import hackman.trevor.copycat.R
import hackman.trevor.copycat.system.ColorResource
import hackman.trevor.copycat.system.getColor
import hackman.trevor.copycat.system.sound.NullSound
import hackman.trevor.copycat.system.sound.Sound

class ColorButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatImageButton(context, attributeSet) {

    var sound: Sound = NullSound

    var pushed = false
        set(value) {
            field = value
            if (value) {
                background.state = intArrayOf(android.R.attr.state_pressed)
                playSound()
            } else {
                background.state = intArrayOf()
            }
        }

    @ColorInt
    private var buttonColor: Int = 0

    init {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.ColorButton, 0, 0).apply {
            buttonColor = getColor(R.styleable.ColorButton_button_color, 0)
        }
    }

    fun playSound() = sound.play()

    fun setColorResource(colorResource: ColorResource) {
        buttonColor = getColor(colorResource)
        createBackground(width, height)
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        if (width != 0 && height != 0) createBackground(width, height)
    }

    private fun createBackground(width: Int, height: Int) {
        background = ColorButtonDrawable(width, height, buttonColor).build()
    }
}
