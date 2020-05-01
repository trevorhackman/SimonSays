package hackman.trevor.copycat.logic.settings

import androidx.annotation.ColorRes
import androidx.annotation.Size
import androidx.annotation.StringRes
import hackman.trevor.copycat.R

enum class ColorSet(
    @StringRes override val nameId: Int,
    @Size(4) @ColorRes val colorResources: List<Int>
) : NameId {
    Classic(R.string.settings_color_classic, classicColors),
    Warm(R.string.settings_color_warm, warmColors),
    Cool(R.string.settings_color_cool, coolColors),
    Royal(R.string.settings_color_royal, royalColors),
    Inverted(R.string.settings_color_inverted, invertedColors),
    Greyed(R.string.settings_color_greyed, greyedColors);
}

private val classicColors =
    listOf(R.color.classic0, R.color.classic1, R.color.classic2, R.color.classic3)
private val warmColors = listOf(R.color.warm0, R.color.warm1, R.color.warm2, R.color.warm3)
private val coolColors = listOf(R.color.cool0, R.color.cool1, R.color.cool2, R.color.cool3)
private val royalColors = listOf(R.color.royal0, R.color.royal1, R.color.royal2, R.color.royal3)
private val invertedColors =
    listOf(R.color.inverted0, R.color.inverted1, R.color.inverted2, R.color.inverted3)
private val greyedColors = listOf(R.color.greyed, R.color.greyed, R.color.greyed, R.color.greyed)
