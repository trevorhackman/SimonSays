package hackman.trevor.copycat.ui.extra_button

import android.content.Context
import android.util.AttributeSet
import hackman.trevor.copycat.R
import hackman.trevor.copycat.system.getDrawable
import hackman.trevor.copycat.system.sound.SoundManager
import hackman.trevor.copycat.ui.settings.SettingsViewModel

class SettingsButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : ExtraButton(context, attributeSet) {

    private lateinit var settingsViewModel: SettingsViewModel

    init {
        background = getDrawable(R.drawable.gear)
    }

    fun setup(settingsViewModel: SettingsViewModel) {
        this.settingsViewModel = settingsViewModel
        setOnClickListener()
    }

    private fun setOnClickListener() = setOnClickListener {
        SoundManager.click.play()
        settingsViewModel.setInBackground(false)
    }
}
