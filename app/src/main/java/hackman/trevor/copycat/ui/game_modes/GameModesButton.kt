package hackman.trevor.copycat.ui.game_modes

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import hackman.trevor.copycat.R
import hackman.trevor.copycat.logic.game.GameMode
import hackman.trevor.copycat.system.SaveData
import hackman.trevor.copycat.system.displayMinimum
import hackman.trevor.copycat.system.getColor
import hackman.trevor.copycat.system.getDrawable
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
    private lateinit var soundManager: SoundManager
    private lateinit var gameMode: GameMode

    init {
        typeface = Typeface.DEFAULT_BOLD
        textSize = displayMinimum() * .016f
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

    fun setup(
        gameModesViewModel: GameModesViewModel,
        soundManager: SoundManager,
        gameMode: GameMode
    ) {
        this.gameModesViewModel = gameModesViewModel
        this.soundManager = soundManager
        this.gameMode = gameMode
        setOnClickListener()
    }

    private fun setOnClickListener() = setOnClickListener {
        SaveData.gameMode = gameMode
        gameModesViewModel.setGameMode(gameMode)
        soundManager.click.play()
    }
}
