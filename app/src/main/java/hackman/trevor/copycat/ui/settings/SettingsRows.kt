package hackman.trevor.copycat.ui.settings

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import hackman.trevor.copycat.R
import hackman.trevor.copycat.logic.enums.ColorSet
import hackman.trevor.copycat.logic.enums.NameId
import hackman.trevor.copycat.logic.enums.Speed
import hackman.trevor.copycat.system.SaveData
import hackman.trevor.copycat.system.getString
import hackman.trevor.copycat.system.sound.SoundManager
import kotlinx.android.synthetic.main.settings_option_row.view.*

@Suppress("LeakingThis")
abstract class SettingsRow<T> @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    private val constants: Array<T>
) : LinearLayout(context, attributeSet) where T : Enum<T>, T : NameId {

    private lateinit var onChangeListener: (T) -> Unit

    private lateinit var soundManager: SoundManager

    protected var optionSelected: T = constants.first()
        set(value) {
            field = value
            settings_option_value.text = getString(value.nameId)
            settings_left_arrow.isEnabled = isLeftArrowEnabled()
            settings_right_arrow.isEnabled = isRightArrowEnabled()
            saveValueSelected()
            onChangeListener(value)
        }

    private fun isLeftArrowEnabled() = optionSelected != constants.first()

    private fun isRightArrowEnabled() = optionSelected != constants.last()

    init {
        View.inflate(context, R.layout.settings_option_row, this)
        settings_left_arrow.setOnClickListener { onLeftArrow() }
        settings_right_arrow.setOnClickListener { onRightArrow() }
    }

    private fun onLeftArrow() {
        if (optionSelected == constants.first()) return
        optionSelected = constants[constants.indexOf(optionSelected) - 1]
        soundManager.click.play()
    }

    private fun onRightArrow() {
        if (optionSelected == constants.last()) return
        optionSelected = constants[constants.indexOf(optionSelected) + 1]
        soundManager.click.play()
    }

    fun setup(soundManager: SoundManager, onChangeListener: (T) -> Unit) {
        this.onChangeListener = onChangeListener
        this.soundManager = soundManager
        initializeValue()
    }

    protected abstract fun initializeValue()

    protected abstract fun saveValueSelected()

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
        optionSelected = SaveData.getInstance(context).speed
    }

    override fun saveValueSelected() {
        SaveData.getInstance(context).speed = optionSelected
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
        optionSelected = SaveData.getInstance(context).colorSet
    }

    override fun saveValueSelected() {
        SaveData.getInstance(context).colorSet = optionSelected
    }
}
