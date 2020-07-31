package hackman.trevor.copycat.logic.game

import hackman.trevor.copycat.R
import hackman.trevor.copycat.logic.settings.ColorSet
import hackman.trevor.copycat.system.StringResource

/** @param buttonNumber IntRange from 0 to 3 representing the four game buttons */
inline class GameButton(val buttonNumber: Int)

fun GameButton?.name(colorSet: ColorSet) = when (colorSet) {
    ColorSet.Classic -> classicName()
    else -> otherName()
}

private fun GameButton?.classicName() = when (this?.buttonNumber) {
    0 -> StringResource(R.string.failure_classic_0)
    1 -> StringResource(R.string.failure_classic_1)
    2 -> StringResource(R.string.failure_classic_2)
    3 -> StringResource(R.string.failure_classic_3)
    else -> StringResource(R.string.failure_null)
}

private fun GameButton?.otherName() = when (this?.buttonNumber) {
    0 -> StringResource(R.string.failure_other_0)
    1 -> StringResource(R.string.failure_other_1)
    2 -> StringResource(R.string.failure_other_2)
    3 -> StringResource(R.string.failure_other_3)
    else -> StringResource(R.string.failure_null)
}
