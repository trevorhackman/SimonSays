package hackman.trevor.copycat.logic.settings

import hackman.trevor.copycat.R
import hackman.trevor.copycat.system.StringResource

enum class Speed(id: Int, val lightDuration: Long, val delayDuration: Long) : NameId {
    Normal(R.string.settings_speed_normal, 500, 90),
    Fast(R.string.settings_speed_fast, 300, 65),
    Extreme(R.string.settings_speed_extreme, 150, 45),
    Insane(R.string.settings_speed_insane, 75, 30);

    override val nameId = StringResource(id)

    val startDelay = lightDuration + delayDuration
}
