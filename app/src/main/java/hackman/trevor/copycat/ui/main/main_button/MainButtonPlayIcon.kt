package hackman.trevor.copycat.ui.main.main_button

import android.content.Context
import android.util.AttributeSet
import hackman.trevor.copycat.R

class MainButtonPlayIcon @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : androidx.appcompat.widget.AppCompatImageView(context, attributeSet) {

    private var animationCount = 0

    init {
        setImageResource(R.drawable.play_symbol)
        gyrate()
    }

    private fun gyrate() {
        val scale = if (animationCount++ % 2 == 0) 0.92f else 1f
        animate().scaleX(scale).scaleY(scale).setDuration(725).withEndAction(::gyrate)
    }
}
