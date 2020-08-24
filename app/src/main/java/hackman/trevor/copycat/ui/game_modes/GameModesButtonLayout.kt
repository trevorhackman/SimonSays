package hackman.trevor.copycat.ui.game_modes

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import hackman.trevor.copycat.R
import hackman.trevor.copycat.logic.game.GameMode
import hackman.trevor.copycat.logic.viewmodels.GameModesViewModel
import hackman.trevor.copycat.system.SaveData
import hackman.trevor.copycat.system.getString
import hackman.trevor.copycat.system.isPortrait

class GameModesButtonLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : ConstraintLayout(context, attributeSet) {

    private lateinit var gameModesViewModel: GameModesViewModel

    private val buttons: List<GameModesButton>
        get() = listOf(
            findViewById(R.id.game_modes_mode_1),
            findViewById(R.id.game_modes_mode_2),
            findViewById(R.id.game_modes_mode_3),
            findViewById(R.id.game_modes_mode_4),
            findViewById(R.id.game_modes_mode_5),
            findViewById(R.id.game_modes_mode_6)
        )

    private var oldIsPortrait = isPortrait()

    init {
        inflateLayout()
        setTextToAllButtons()
    }

    private fun inflateLayout() = View.inflate(
        context,
        if (oldIsPortrait) R.layout.game_modes_buttons_portrait else R.layout.game_modes_buttons_landscape,
        this
    )

    // Find max width of buttons and set to all buttons for a consistent width
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (orientationChanged()) {
            updateOrientation()
            removeAllViews()
            inflateLayout()
            setTextToAllButtons()
            setupAll()
        }

        val buttonWidth = findMaxButtonWidth()
        for (button in buttons) button.width = buttonWidth
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    private fun orientationChanged() = oldIsPortrait != isPortrait()

    private fun updateOrientation() {
        oldIsPortrait = isPortrait()
    }

    private fun findMaxButtonWidth(): Int = buttons.map {
        val widthSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        val heightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        it.measure(widthSpec, heightSpec)
        it.measuredWidth
    }.max()!!

    private fun setTextToAllButtons() = buttons.forEachIndexed { index, button ->
        button.text = getString(GameMode.values()[index].name())
    }

    fun setup(gameModesViewModel: GameModesViewModel) {
        this.gameModesViewModel = gameModesViewModel
        setupAll()
    }

    private fun setupAll() {
        setupButtons()
        setSelectedMode(SaveData.gameMode)
    }

    private fun setupButtons() = buttons.forEachIndexed { index, button ->
        button.setup(GameMode.values()[index])
    }

    private fun GameModesButton.setup(gameMode: GameMode) = setup(gameModesViewModel, gameMode)

    fun setSelectedMode(gameMode: GameMode) {
        val highlightedIndex = GameMode.values().indexOf(gameMode)
        buttons.forEachIndexed { index, button ->
            button.highlighted = index == highlightedIndex
        }
    }
}
