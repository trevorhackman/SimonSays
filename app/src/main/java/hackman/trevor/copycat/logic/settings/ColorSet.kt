package hackman.trevor.copycat.logic.settings

import hackman.trevor.copycat.R
import hackman.trevor.copycat.system.ColorResource
import hackman.trevor.copycat.system.StringResource

enum class ColorSet(
    id: Int,
    val colors: ColorQuad
) : NameId {
    Classic(R.string.settings_color_classic, classicColors),
    Warm(R.string.settings_color_warm, warmColors),
    Cool(R.string.settings_color_cool, coolColors),
    Royal(R.string.settings_color_royal, royalColors),
    Inverted(R.string.settings_color_inverted, invertedColors),
    Greyed(R.string.settings_color_greyed, greyedColors);

    override val nameId = StringResource(id)
}

data class ColorQuad(
    val topLeft: ColorResource,
    val topRight: ColorResource,
    val bottomLeft: ColorResource,
    val bottomRight: ColorResource
) {
    constructor(topLeft: Int, topRight: Int, bottomLeft: Int, bottomRight: Int) : this(
        ColorResource(topLeft),
        ColorResource(topRight),
        ColorResource(bottomLeft),
        ColorResource(bottomRight)
    )
}

private val classicColors =
    ColorQuad(R.color.classic0, R.color.classic1, R.color.classic2, R.color.classic3)
private val warmColors = ColorQuad(R.color.warm0, R.color.warm1, R.color.warm2, R.color.warm3)
private val coolColors = ColorQuad(R.color.cool0, R.color.cool1, R.color.cool2, R.color.cool3)
private val royalColors = ColorQuad(R.color.royal0, R.color.royal1, R.color.royal2, R.color.royal3)
private val invertedColors =
    ColorQuad(R.color.inverted0, R.color.inverted1, R.color.inverted2, R.color.inverted3)
private val greyedColors = ColorQuad(R.color.greyed, R.color.greyed, R.color.greyed, R.color.greyed)
