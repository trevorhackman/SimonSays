package hackman.trevor.copycat.logic.settings

import hackman.trevor.copycat.R
import hackman.trevor.copycat.system.StringResource
import hackman.trevor.copycat.system.sound.SoundManager

enum class FailureSound(id: Int): NameId {
    ClassicError(R.string.settings_failure_error_tone),
    BitGenerated(R.string.settings_failure_bit_generated);

    override val nameId = StringResource(id)
}

fun FailureSound.toSound() = when(this) {
    FailureSound.ClassicError -> SoundManager.failure_error
    FailureSound.BitGenerated -> SoundManager.failure_bit
}
