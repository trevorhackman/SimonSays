package hackman.trevor.copycat.ui.game_modes

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import hackman.trevor.copycat.logic.GameMode
import hackman.trevor.copycat.system.SaveData
import hackman.trevor.copycat.system.displayMinimum
import hackman.trevor.copycat.system.getString

class GameModesDescription @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatTextView(context, attributeSet) {

    private val placeholder = TextView(context)

    init {
        text = getString(SaveData(context).gameMode.description())
        setTextSize(TypedValue.COMPLEX_UNIT_PX, displayMinimum() * .05f)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val maxHeight = MeasureSpec.makeMeasureSpec(findMaxHeight(), MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, maxHeight)
    }

    /**
     * To prevent a shifting height from varying descriptions, find max height of TextView with
     * any game mode description
     */
    private fun findMaxHeight(): Int =
        GameMode.values().map { measureHeightWith(getString(it.description())) }.max()
            ?: error("Impossible")

    private fun measureHeightWith(text: String): Int {
        placeholder.layoutParams = layoutParams
        placeholder.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        placeholder.text = text

        val widthSpec = MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY)
        val heightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        placeholder.measure(widthSpec, heightSpec)

        return placeholder.measuredHeight
    }
}
