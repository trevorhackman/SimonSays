package hackman.trevor.copycat.logic.settings

import androidx.annotation.StringRes
import hackman.trevor.copycat.R

enum class Speed(@StringRes override val nameId: Int) :
    NameId {
    Normal(R.string.settings_speed_normal),
    Fast(R.string.settings_speed_fast),
    Extreme(R.string.settings_speed_extreme),
    Insane(R.string.settings_speed_insane);
}
