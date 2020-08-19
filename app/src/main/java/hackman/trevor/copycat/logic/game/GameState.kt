package hackman.trevor.copycat.logic.game

enum class GameState {
    MainMenu,
    Watch,
    Input,
    Failure;

    val inGame
        get() = this == Watch || this == Input
}
