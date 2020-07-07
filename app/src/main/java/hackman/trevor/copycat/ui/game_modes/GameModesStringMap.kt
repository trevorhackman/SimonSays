package hackman.trevor.copycat.ui.game_modes

import hackman.trevor.copycat.R
import hackman.trevor.copycat.logic.game.GameMode
import hackman.trevor.copycat.system.StringResource

fun GameMode.name() = StringResource(
    when (this) {
        GameMode.Classic -> R.string.game_modes_mode_classic
        GameMode.Reverse -> R.string.game_modes_mode_reverse
        GameMode.Chaos -> R.string.game_modes_mode_chaos
        GameMode.Single -> R.string.game_modes_mode_single
        GameMode.Opposite -> R.string.game_modes_mode_opposite
        GameMode.TwoPlayer -> R.string.game_modes_mode_two_player
    }
)

fun GameMode.description() = StringResource(
    when (this) {
        GameMode.Classic -> R.string.game_modes_mode_classic_description
        GameMode.Reverse -> R.string.game_modes_mode_reverse_description
        GameMode.Chaos -> R.string.game_modes_mode_chaos_description
        GameMode.Single -> R.string.game_modes_mode_single_description
        GameMode.Opposite -> R.string.game_modes_mode_opposite_description
        GameMode.TwoPlayer -> R.string.game_modes_mode_two_player_description
    }
)

fun GameMode.popupText() = StringResource(
    when (this) {
        GameMode.Classic -> R.string.game_modes_mode_classic_popup
        GameMode.Reverse -> R.string.game_modes_mode_reverse_popup
        GameMode.Chaos -> R.string.game_modes_mode_chaos_popup
        GameMode.Single -> R.string.game_modes_mode_single_popup
        GameMode.Opposite -> R.string.game_modes_mode_opposite_popup
        GameMode.TwoPlayer -> R.string.game_modes_mode_two_player_popup
    }
)
