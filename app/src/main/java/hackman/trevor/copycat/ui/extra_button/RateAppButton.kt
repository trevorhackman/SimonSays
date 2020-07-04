package hackman.trevor.copycat.ui.extra_button

import android.content.Context
import android.util.AttributeSet
import hackman.trevor.copycat.R
import hackman.trevor.copycat.system.RateTheApp
import hackman.trevor.copycat.system.getDrawable
import hackman.trevor.copycat.system.sound.SoundManager
import hackman.trevor.copycat.ui.DialogFactory

class RateAppButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : ExtraButton(context, attributeSet) {

    private val rateTheAppDialog by lazy {
        DialogFactory.rateTheApp {
            RateTheApp.startRateAppIntent(context)
        }
    }

    init {
        background = getDrawable(R.drawable.star)
        setOnClickListener {
            SoundManager.click.play()
            rateTheAppDialog.show()
        }
    }
}
