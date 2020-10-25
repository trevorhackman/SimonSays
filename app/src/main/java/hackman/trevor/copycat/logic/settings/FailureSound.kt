package hackman.trevor.copycat.logic.settings

import hackman.trevor.copycat.R
import hackman.trevor.copycat.system.StringResource
import hackman.trevor.copycat.system.sound.SoundManager

enum class FailureSound(id: Int): NameId {
    BitGenerated(R.string.settings_failure_bit_generated),
    ClassicError(R.string.settings_failure_error_tone);

    override val nameId = StringResource(id)
}

fun FailureSound.toSound() = when(this) {
    FailureSound.BitGenerated -> SoundManager.failure_bit
    FailureSound.ClassicError -> SoundManager.failure_error
}
