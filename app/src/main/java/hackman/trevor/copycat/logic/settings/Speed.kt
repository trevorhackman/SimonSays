package hackman.trevor.copycat.logic.settings

import hackman.trevor.copycat.R
import hackman.trevor.copycat.system.StringResource

enum class Speed(id: Int) : NameId {
    Normal(R.string.settings_speed_normal),
    Fast(R.string.settings_speed_fast),
    Extreme(R.string.settings_speed_extreme),
    Insane(R.string.settings_speed_insane);

    override val nameId = StringResource(id)
}
