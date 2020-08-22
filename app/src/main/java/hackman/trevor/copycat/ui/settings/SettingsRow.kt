package hackman.trevor.copycat.ui.settings

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.setMargins
import hackman.trevor.copycat.R
import hackman.trevor.copycat.logic.settings.ColorSet
import hackman.trevor.copycat.logic.settings.FailureSound
import hackman.trevor.copycat.logic.settings.NameId
import hackman.trevor.copycat.logic.settings.Speed
import hackman.trevor.copycat.logic.viewmodels.SettingsViewModel
import hackman.trevor.copycat.system.SaveData
import hackman.trevor.copycat.system.displayMinimum
import hackman.trevor.copycat.system.getString
import hackman.trevor.copycat.system.sound.SoundManager
import kotlinx.android.synthetic.main.settings_option_row.view.*

@Suppress("LeakingThis")
abstract class SettingsRow<T> @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    private val constants: Array<T>
) : LinearLayout(context, attributeSet) where T : Enum<T>, T : NameId {

    protected lateinit var settingsViewModel: SettingsViewModel

    protected var optionSelected: T = constants.first()
        set(value) {
            field = value
            settings_option_value.text = getString(value.nameId)
            settings_left_arrow.isEnabled = isLeftArrowEnabled()
            settings_right_arrow.isEnabled = isRightArrowEnabled()
            saveValueSelected()
            if (::settingsViewModel.isInitialized) onChange()
        }

    private fun isLeftArrowEnabled() = optionSelected != constants.first()

    private fun isRightArrowEnabled() = optionSelected != constants.last()

    init {
        View.inflate(context, R.layout.settings_option_row, this)
        (settings_option_row.layoutParams as LayoutParams).setMargins((displayMinimum() * 0.01).toInt())
        settings_left_arrow.setOnClickListener { onLeftArrow() }
        settings_right_arrow.setOnClickListener { onRightArrow() }
    }

    private fun onLeftArrow() {
        if (optionSelected == constants.first()) return
        optionSelected = constants[constants.indexOf(optionSelected) - 1]
        SoundManager.click.play()
    }

    private fun onRightArrow() {
        if (optionSelected == constants.last()) return
        optionSelected = constants[constants.indexOf(optionSelected) + 1]
        SoundManager.click.play()
    }

    fun setup(settingsViewModel: SettingsViewModel) {
        this.settingsViewModel = settingsViewModel
        initializeValue()
    }

    protected abstract fun initializeValue()

    protected abstract fun saveValueSelected()

    // Immediate change upon option
    protected open fun onChange() {}

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        settings_left_arrow.isEnabled = enabled && isLeftArrowEnabled()
        settings_right_arrow.isEnabled = enabled && isRightArrowEnabled()
    }
}

class SettingsSpeedRow @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : SettingsRow<Speed>(context, attributeSet, Speed.values()) {

    init {
        settings_option.text = getString(R.string.settings_setting_speed)
    }

    override fun initializeValue() {
        optionSelected = SaveData.speed
    }

    override fun saveValueSelected() {
        SaveData.speed = optionSelected
    }
}

class SettingsColorRow @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : SettingsRow<ColorSet>(context, attributeSet, ColorSet.values()) {

    init {
        settings_option.text = getString(R.string.settings_setting_color)
    }

    override fun initializeValue() {
        optionSelected = SaveData.colorSet
    }

    override fun saveValueSelected() {
        SaveData.colorSet = optionSelected
    }

    override fun onChange() {
        settingsViewModel.setColorSet(optionSelected)
    }
}

class SettingsFailureSoundRow @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : SettingsRow<FailureSound>(context, attributeSet, FailureSound.values()) {

    init {
        settings_option.text = getString(R.string.settings_setting_failure_sound)
    }

    override fun initializeValue() {
        optionSelected = SaveData.failureSound
    }

    override fun saveValueSelected() {
        SaveData.failureSound = optionSelected
    }

    override fun onChange() {
        settingsViewModel.setFailureSound(optionSelected)
    }
}
