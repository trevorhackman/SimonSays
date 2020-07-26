package hackman.trevor.copycat.ui.failure

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import hackman.trevor.copycat.R

class FailureBody @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : LinearLayout(context, attributeSet) {

    init {
        View.inflate(context, R.layout.failure_body, this)
    }
}
