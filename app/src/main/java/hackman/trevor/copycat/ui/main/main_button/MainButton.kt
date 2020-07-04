package hackman.trevor.copycat.ui.main.main_button

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import hackman.trevor.copycat.R
import hackman.trevor.copycat.ui.fadeIn
import hackman.trevor.copycat.ui.fadeOut
import hackman.trevor.copycat.ui.main.CircularTouchListener
import kotlinx.android.synthetic.main.main_button_frame_layout.view.*

class MainButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : FrameLayout(context, attributeSet) {

    init {
        View.inflate(context, R.layout.main_button_frame_layout, this)
        setOnTouchListener(CircularTouchListener)
    }

    override fun performClick(): Boolean {
        super.performClick()

        main_button_play_icon.fadeOut()
        main_button_text.fadeIn()

        return true
    }
}
