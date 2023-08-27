package hackman.trevor.copycat.ui.main.main_button

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import hackman.trevor.copycat.R
import hackman.trevor.copycat.system.displayMinimum
import hackman.trevor.copycat.system.getDrawable
import hackman.trevor.copycat.system.pixelTextSize
import kotlin.math.pow

class MainButtonFrameLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : FrameLayout(context, attributeSet) {

    private lateinit var mainButtonLinearLayout: LinearLayout
    private lateinit var mainButtonRoundText: TextView

    init {
        background = getDrawable(R.drawable.circle)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        mainButtonLinearLayout = findViewById(R.id.main_button_linear_layout)
        mainButtonRoundText = findViewById(R.id.main_button_round_text)
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
    private fun sizePlayIcon(layoutSize: Int) = with(mainButtonLinearLayout.layoutParams) {
        val size = (layoutSize / (2.0.pow(.5))).toInt()
        width = size
        height = size
    }

    private fun setRoundTextSize(layoutSize: Int) {
        mainButtonRoundText.pixelTextSize = layoutSize * .5f
    }

}
