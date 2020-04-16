package hackman.trevor.copycat.ui.color_button

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageButton
import hackman.trevor.copycat.R
import hackman.trevor.copycat.system.sound.Sound

class ColorButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatImageButton(context, attributeSet) {

    private lateinit var sound: Sound
    private var buttonColor: Int = 0

    init {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.ColorButton, 0, 0).apply {
            buttonColor = getColor(R.styleable.ColorButton_button_color, 0)
        }
    }

    fun setup(sound: Sound? = null, colorInt: Int? = null) {
        sound?.let { this.sound = sound }
        colorInt?.let {
            buttonColor = it
            createBackground(width, height)
        }

        setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> playSound()
            }
            view?.onTouchEvent(event) ?: true
        }
    }

    private fun playSound() = sound.play()

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        if (width != 0 && height != 0) createBackground(width, height)
    }

    private fun createBackground(width: Int, height: Int) {
        background = ColorButtonDrawable(
            width,
            height,
            buttonColor
        ).make()
    }
}
