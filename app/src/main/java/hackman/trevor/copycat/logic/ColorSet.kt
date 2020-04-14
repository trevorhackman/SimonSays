package hackman.trevor.copycat.logic

import androidx.annotation.StringRes
import hackman.trevor.copycat.R

enum class ColorSet(@StringRes override val nameId: Int) : NameId {
    Classic(R.string.settings_color_classic),
    Warm(R.string.settings_color_warm),
    Blues(R.string.settings_color_cool),
    Purples(R.string.settings_color_royal),
    Inverted(R.string.settings_color_inverted),
    Greyed(R.string.settings_color_greyed);
}
