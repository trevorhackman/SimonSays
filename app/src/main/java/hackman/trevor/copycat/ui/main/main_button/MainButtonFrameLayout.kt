package hackman.trevor.copycat.ui.main.main_button

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.FrameLayout
import hackman.trevor.copycat.R
import hackman.trevor.copycat.system.displayMinimum
import hackman.trevor.copycat.system.getDrawable
import kotlinx.android.synthetic.main.main_button_frame_layout.view.*
import kotlin.math.pow

class MainButtonFrameLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : FrameLayout(context, attributeSet) {

    init {
        background = getDrawable(R.drawable.circle)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val layoutSize = (displayMinimum() * .40).toInt()

        val measureSpec = MeasureSpec.makeMeasureSpec(layoutSize, MeasureSpec.EXACTLY)
        super.onMeasure(measureSpec, measureSpec)

        setSizeOfChildren(layoutSize)
    }

    private fun setSizeOfChildren(layoutSize: Int) {
        sizePlayIcon(layoutSize)
        setRoundTextSize(layoutSize)
    }

    /**
     * Inscribed square inside inscribed circle inside square
     * Divide by sqrt 2 to go from outer square's length to inner square's length
     */
    private fun sizePlayIcon(layoutSize: Int) = with(main_button_play_icon.layoutParams) {
        val size = (layoutSize / (2.0.pow(.5))).toInt()
        width = size
        height = size
    }

    private fun setRoundTextSize(size: Int) = main_button_text.setTextSize(TypedValue.COMPLEX_UNIT_PX, size * .5f)
}
