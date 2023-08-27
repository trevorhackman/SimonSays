package hackman.trevor.copycat.ui.color_button

import android.content.Context
import android.util.AttributeSet
import android.widget.TextSwitcher
import androidx.core.view.children
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import hackman.trevor.copycat.logic.game.GameMode
import hackman.trevor.copycat.logic.viewmodels.GameViewModel
import hackman.trevor.copycat.observe
import hackman.trevor.copycat.system.getString

/**
 * Animates text change with two child [InfoText] TextViews
 */
class InfoTextSwitcher @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : TextSwitcher(context, attributeSet), LifecycleOwner {

    private lateinit var gameViewModel: GameViewModel
    override lateinit var lifecycle: Lifecycle

    private val infoTextChildren: List<InfoText>
        get() = children.toList().map { it as InfoText }

    fun setup(gameViewModel: GameViewModel, lifecycle: Lifecycle) {
        this.gameViewModel = gameViewModel
        this.lifecycle = lifecycle
        observeGame()
        observeInfoText()
    }

    private fun observeGame() = observe(gameViewModel.gameState) { gameState ->
        if (gameViewModel.gameMode.value != GameMode.TwoPlayer) return@observe
        if (gameState.inGame) {
            infoTextChildren.forEach { it.shrinked = false }
        } else {
            infoTextChildren.forEach { it.shrinked = true }
        }
    }

    private fun observeInfoText() = observe(gameViewModel.infoText) {
        setText(getString(it))
    }
}
