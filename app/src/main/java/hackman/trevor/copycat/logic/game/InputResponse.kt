package hackman.trevor.copycat.logic.game

sealed class InputResponse

object InputSuccessResponse : InputResponse()

/**
 * @param correct The correct button that should've been pressed.
 * Null if no correct button, happens if there's extra input.
 */
class InputFailedResponse(val correct: GameButton?) : InputResponse()
