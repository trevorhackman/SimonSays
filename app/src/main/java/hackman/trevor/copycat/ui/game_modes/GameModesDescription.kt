package hackman.trevor.copycat.ui.game_modes

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import hackman.trevor.copycat.logic.game.GameMode
import hackman.trevor.copycat.system.SaveData
import hackman.trevor.copycat.system.displayMinimum
import hackman.trevor.copycat.system.getString
import hackman.trevor.copycat.system.pixelTextSize

class GameModesDescription @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatTextView(context, attributeSet) {

    private val placeholder = TextView(context)

    init {
        pixelTextSize = displayMinimum() * .05f
        updateText(SaveData.gameMode)
    }

    fun updateText(gameMode: GameMode) {
        text = getString(gameMode.description())
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // This is super necessary for measuredWidth to have correct value when finding max height
        // Placeholder is given width of this view, then measure max height of placeholder with varying text,
        // But we don't know width of this view until one regular measurement pass is done
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val maxHeight = MeasureSpec.makeMeasureSpec(findMaxHeight(), MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, maxHeight)
    }

    /**
     * To prevent a shifting height from varying descriptions, find max height of TextView with
     * any game mode description
     */
    private fun findMaxHeight() = GameMode.entries.map { measureHeightWith(getString(it.description())) }.maxOrNull()!!

    private fun measureHeightWith(text: String): Int {
        placeholder.layoutParams = layoutParams
        placeholder.pixelTextSize = textSize
        placeholder.text = text

        val widthSpec = MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY)
        val wrapHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        placeholder.measure(widthSpec, wrapHeightSpec)

        return placeholder.measuredHeight
    }
}
