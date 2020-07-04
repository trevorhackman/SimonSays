package hackman.trevor.copycat.ui.game_modes

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import hackman.trevor.copycat.R
import hackman.trevor.copycat.logic.game.GameMode
import hackman.trevor.copycat.system.SaveData
import hackman.trevor.copycat.system.getString
import hackman.trevor.copycat.system.sound.SoundManager
import kotlinx.android.synthetic.main.game_modes_buttons.view.*

class GameModesButtons @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : ConstraintLayout(context, attributeSet) {

    private lateinit var gameModesViewModel: GameModesViewModel
    private lateinit var soundManager: SoundManager

    private val buttons: List<GameModesButton> by lazy {
        listOf(
            game_modes_mode_1,
            game_modes_mode_2,
            game_modes_mode_3,
            game_modes_mode_4,
            game_modes_mode_5,
            game_modes_mode_6
        )
    }

    init {
        View.inflate(context, R.layout.game_modes_buttons, this)
        setText()
        setSelectedMode(SaveData.gameMode)
    }

    private fun setText() = buttons.forEachIndexed { index, button ->
        button.text = getString(GameMode.values()[index].name())
    }

    fun setup(gameModesViewModel: GameModesViewModel, soundManager: SoundManager) {
        this.gameModesViewModel = gameModesViewModel
        this.soundManager = soundManager
        setupButtons()
    }

    private fun setupButtons() = buttons.forEachIndexed { index, button ->
        button.setup(GameMode.values()[index])
    }

    private fun GameModesButton.setup(gameMode: GameMode) =
        setup(gameModesViewModel, soundManager, gameMode)

    fun setSelectedMode(gameMode: GameMode) {
        val highlightedIndex = GameMode.values().indexOf(gameMode)
        buttons.forEachIndexed { index, button ->
            button.highlighted = index == highlightedIndex
        }
    }

    /** Find max width of buttons and set to all buttons for a consistent width */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val buttonWidth = findMaxButtonWidth()
        for (button in buttons) button.width = buttonWidth
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    private fun findMaxButtonWidth(): Int = buttons.map {
        val widthSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        val heightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        it.measure(widthSpec, heightSpec)
        it.measuredWidth
    }.max()!!
}
