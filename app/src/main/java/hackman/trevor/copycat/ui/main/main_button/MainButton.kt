package hackman.trevor.copycat.ui.main.main_button

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import hackman.trevor.copycat.R
import hackman.trevor.copycat.ui.main.CircularTouchListener

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

        // TODO ACTION
        Log.e("TREVOR", "CLICK DETECTED")

        return true
    }
}
