package hackman.trevor.copycat.ui.game_modes

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import hackman.trevor.copycat.R
import hackman.trevor.copycat.logic.game.GameMode
import hackman.trevor.copycat.logic.viewmodels.GameModesViewModel
import hackman.trevor.copycat.system.*
import hackman.trevor.copycat.system.sound.SoundManager

class GameModesButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatButton(context, attributeSet) {

    var highlighted = false
        set(value) {
            val oldField = field
            field = value
            if (oldField != field) updateBackground()
        }

    private lateinit var gameModesViewModel: GameModesViewModel
    private lateinit var gameMode: GameMode

    init {
        typeface = Typeface.DEFAULT_BOLD
        pixelTextSize = displayMinimum() * .045f
        updateBackground()
    }

    private fun updateBackground() {
        if (highlighted) {
            background = getDrawable(R.drawable.bordered_rectangle_highlighted)
            setTextColor(getColor(R.color.GreenAccent700))
        } else {
            background = getDrawable(R.drawable.bordered_rectangle_light)
            setTextColor(getColor(R.color.Black))
        }
    }

    fun setup(gameModesViewModel: GameModesViewModel, gameMode: GameMode) {
        this.gameModesViewModel = gameModesViewModel
        this.gameMode = gameMode
        setOnClickListener()
    }

    private fun setOnClickListener() = setOnClickListener {
        SoundManager.click.play()
        SaveData.gameMode = gameMode
        gameModesViewModel.setGameMode(gameMode)
    }
}
