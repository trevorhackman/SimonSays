package hackman.trevor.copycat.ui.main

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatButton
import hackman.trevor.copycat.R
import hackman.trevor.copycat.system.*
import hackman.trevor.copycat.system.sound.SoundManager
import hackman.trevor.copycat.ui.game_modes.GameModesViewModel
import kotlin.math.min

class GameModesButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatButton(context, attributeSet) {

    private lateinit var soundManager: SoundManager
    private lateinit var gameModesViewModel: GameModesViewModel

    init {
        text = getString(R.string.game_modes)
        setTextSize(TypedValue.COMPLEX_UNIT_PX, displayMinimum() * .075f)
        setTextColor(Color.White)
    }

    fun setup(soundManager: SoundManager, gameModesViewModel: GameModesViewModel) {
        this.soundManager = soundManager
        this.gameModesViewModel = gameModesViewModel
        setOnClickListener()
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        if (width != 0 && height != 0) background = createBackground(width, height)
    }

    private fun createBackground(width: Int, height: Int): Drawable {
        val min = min(width, height)
        val radius = min * .4f
        val strokeWidth = dpToPixel(6)

        return createRippleDrawable(GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(Color.Grey900)
            cornerRadius = radius
            setStroke(strokeWidth, Color.Black)
        }, Color.White)
    }

    private fun setOnClickListener() =
        setOnClickListener {
            soundManager.click.play()
            gameModesViewModel.setInBackground(false)
        }
}
