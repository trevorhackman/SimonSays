package hackman.trevor.copycat.ui

import android.content.Context
import android.util.AttributeSet
import hackman.trevor.copycat.R
import hackman.trevor.copycat.system.getDrawable

class Divider @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : androidx.appcompat.widget.AppCompatImageView(context, attributeSet) {
    init {
        background = getDrawable(R.drawable.block)
    }
}
