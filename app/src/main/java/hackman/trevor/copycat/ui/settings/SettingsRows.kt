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
import kotlinx.android.synthetic.main.settings_option_row.view.*

@Suppress("LeakingThis")
abstract class SettingsRow<T> @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    clazz: Class<T>
) : LinearLayout(context, attributeSet) where T : Enum<T>, T : NameId {
    private val constants: Array<T> = clazz.enumConstants as Array<T>

    var optionSelected: T = constants.first()
        set(value) {
            field = value
            settings_option_value.text = getString(value.nameId)
            settings_left_arrow.isEnabled = value != constants.first()
            settings_right_arrow.isEnabled = value != constants.last()
        }

    init {
        View.inflate(context, R.layout.settings_option_row, this)
    }
}

class SettingsSpeedRow @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : SettingsRow<Speed>(context, attributeSet, Speed::class.java) {

    init {
        settings_option.text = getString(R.string.settings_setting_speed)
        optionSelected = SaveData.getInstance(context).speed
    }
}

class SettingsColorRow @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : SettingsRow<ColorSet>(context, attributeSet, ColorSet::class.java) {

    init {
        settings_option.text = getString(R.string.settings_setting_color)
        optionSelected = SaveData.getInstance(context).colorSet
    }
}