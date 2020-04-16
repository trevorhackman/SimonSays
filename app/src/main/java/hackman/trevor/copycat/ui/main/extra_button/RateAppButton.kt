package hackman.trevor.copycat.ui.main.extra_button

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

    private lateinit var soundManager: SoundManager

    private val rateTheAppDialog by lazy {
        DialogFactory(context).rateTheApp {
            RateTheApp.startRateAppIntent(context)
        }
    }

    init {
        background = getDrawable(R.drawable.star)
        setOnClickListener {
            soundManager.click.play()
            rateTheAppDialog.show()
        }
    }

    fun setup(soundManager: SoundManager) {
        this.soundManager = soundManager
    }
}
