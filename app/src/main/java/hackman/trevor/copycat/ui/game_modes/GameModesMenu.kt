package hackman.trevor.copycat.ui.game_modes

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import hackman.trevor.copycat.R
import hackman.trevor.copycat.observe
import hackman.trevor.copycat.system.SaveData
import hackman.trevor.copycat.system.displayHeight
import hackman.trevor.copycat.system.displayWidth
import hackman.trevor.copycat.system.getString
import hackman.trevor.copycat.system.sound.SoundManager
import hackman.trevor.copycat.ui.fadeIn
import hackman.trevor.copycat.ui.fadeOut
import kotlinx.android.synthetic.main.game_modes_menu.view.*
import kotlin.math.min

class GameModesMenu @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : ConstraintLayout(context, attributeSet), LifecycleOwner {

    private lateinit var gameModesViewModel: GameModesViewModel
    private lateinit var lifecycleOwner: LifecycleOwner
    private lateinit var soundManager: SoundManager

    init {
        View.inflate(context, R.layout.game_modes_menu, this)
    }

    fun setup(
        gameModesViewModel: GameModesViewModel,
        lifecycleOwner: LifecycleOwner,
        soundManager: SoundManager
    ) {
        this.gameModesViewModel = gameModesViewModel
        this.lifecycleOwner = lifecycleOwner
        this.soundManager = soundManager
        initGameMode()
        setupButtons()
        setupCloseButton()
        observeGameMode()
        observeInBackground()
    }

    private fun initGameMode() = gameModesViewModel.setGameMode(SaveData(context).gameMode)

    private fun setupButtons() = game_modes_buttons.setup(gameModesViewModel, soundManager)

    private fun setupCloseButton() =
        game_modes_close_button.setOnClickListener {
            gameModesViewModel.setInBackground(true)
            soundManager.click.play()
        }

    private fun observeGameMode() = observe(gameModesViewModel.gameMode) {
        game_modes_description.text = getString(it.description())
        game_modes_buttons.setSelectedMode(it)
    }

    private fun observeInBackground() = observe(gameModesViewModel.inBackground) {
        if (it) fadeOut()
        else fadeIn()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.makeMeasureSpec(determineWidth(), MeasureSpec.EXACTLY)
        super.onMeasure(width, heightMeasureSpec)
    }

    private fun determineWidth() = min(displayWidth(), (displayWidth() + displayHeight()) / 2)

    override fun getLifecycle(): Lifecycle = lifecycleOwner.lifecycle
}
