package hackman.trevor.copycat.logic

/**
 * The response to an input
 * @param isSuccess True if correct button pressed at correct time. False otherwise.
 * @param correct The correct button that should've been pressed if failed. Null if success.
 */
data class InputResponse(
    val isSuccess: Boolean,
    val correct: Int? = null
)
