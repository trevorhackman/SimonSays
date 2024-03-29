package hackman.trevor.copycat.logic.game

import hackman.trevor.copycat.R
import hackman.trevor.copycat.logic.settings.ColorSet
import hackman.trevor.copycat.system.StringResource

/** @param buttonNumber IntRange from 0 to 3 representing the four game buttons */
@JvmInline
value class GameButton(val buttonNumber: Int)

fun GameButton?.name(colorSet: ColorSet) = when (colorSet) {
    ColorSet.Normal -> normalName()
    else -> otherName()
}

private fun GameButton?.normalName() = when (this?.buttonNumber) {
    0 -> StringResource(R.string.failure_normal_0)
    1 -> StringResource(R.string.failure_normal_1)
    2 -> StringResource(R.string.failure_normal_2)
    3 -> StringResource(R.string.failure_normal_3)
    else -> StringResource(R.string.failure_null)
}

private fun GameButton?.otherName() = when (this?.buttonNumber) {
    0 -> StringResource(R.string.failure_other_0)
    1 -> StringResource(R.string.failure_other_1)
    2 -> StringResource(R.string.failure_other_2)
    3 -> StringResource(R.string.failure_other_3)
    else -> StringResource(R.string.failure_null)
}
