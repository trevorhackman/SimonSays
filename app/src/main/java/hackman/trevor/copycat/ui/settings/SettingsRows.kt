package hackman.trevor.copycat.ui.settings

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import hackman.trevor.copycat.R
import hackman.trevor.copycat.logic.ColorSet
import hackman.trevor.copycat.logic.NameId
import hackman.trevor.copycat.logic.Speed
import hackman.trevor.copycat.system.SaveData
import hackman.trevor.copycat.system.getString
import kotlinx.android.synthetic.main.settings_option_row.view.*

abstract class SettingsRow<T> @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    options: Array<T>
) : LinearLayout(context, attributeSet) where T : Enum<T>, T : NameId {
    var optionSelected: T = options[0]
        set(value) {
            field = value
            settings_option_value.text = getString(value.nameId)
        }

    init {
        View.inflate(context, R.layout.settings_option_row, null)
    }
}

class SettingsSpeedRow @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : SettingsRow<Speed>(context, attributeSet, Speed.values()) {

    init {
        settings_option.text = getString(R.string.settings_setting_speed)
        settings_option_value.text = getString(SaveData.getInstance(context).speed.nameId)
    }
}

class SettingsColorRow @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : SettingsRow<ColorSet>(context, attributeSet, ColorSet.values()) {

    init {
        settings_option.text = getString(R.string.settings_setting_color)
        settings_option_value.text = getString(SaveData.getInstance(context).colorSet.nameId)
    }
}