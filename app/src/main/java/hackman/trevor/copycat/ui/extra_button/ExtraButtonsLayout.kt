package hackman.trevor.copycat.ui.extra_button

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewPropertyAnimator
import androidx.constraintlayout.widget.ConstraintLayout
import hackman.trevor.copycat.R
import hackman.trevor.copycat.logic.viewmodels.GameModesViewModel
import hackman.trevor.copycat.logic.viewmodels.RemoveAdsViewModel
import hackman.trevor.copycat.logic.viewmodels.SettingsViewModel
import hackman.trevor.copycat.system.isPortrait

class ExtraButtonsLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : ConstraintLayout(context, attributeSet) {

    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var gameModesViewModel: GameModesViewModel
    private lateinit var removeAdsViewModel: RemoveAdsViewModel

    private var oldIsPortrait = isPortrait()

    init {
        inflateLayout()
    }

    private fun inflateLayout() = View.inflate(
        context,
        if (oldIsPortrait) R.layout.extra_buttons_portrait else R.layout.extra_buttons_landscape,
        this
    )

    // Checks for orientation change before animation,
    // that way the new layout triggered by orientation change
    // doesn't interrupt the animation
    fun animate(animation: ExtraButtonsLayout.() -> ViewPropertyAnimator) {
        ifOrientationChanged()
        animation()
    }

    fun setup(
        settingsViewModel: SettingsViewModel,
        gameModesViewModel: GameModesViewModel,
        removeAdsViewModel: RemoveAdsViewModel
    ) {
        this.settingsViewModel = settingsViewModel
        this.gameModesViewModel = gameModesViewModel
        this.removeAdsViewModel = removeAdsViewModel
        setupAll()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        ifOrientationChanged()
    }

    private fun ifOrientationChanged() {
        if (orientationChanged()) {
            updateOrientation()
            removeAllViews()
            inflateLayout()
            setupAll()
        }
    }

    private fun orientationChanged() = oldIsPortrait != isPortrait()

    private fun updateOrientation() {
        oldIsPortrait = isPortrait()
    }

    private fun setupAll() {
        setupSettingsButton()
        setupGameModesButton()
        setupNoAdsButton()
    }

    private fun setupSettingsButton() = findSettingsButton().setup(settingsViewModel)

    private fun setupGameModesButton() = findGameModesButton().setup(gameModesViewModel)

    private fun setupNoAdsButton() = findNoAdsButton().setup(removeAdsViewModel)

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        findMoreGamesButton().isEnabled = enabled
        findNoAdsButton().isEnabled = enabled
        findRateAppButton().isEnabled = enabled
        findSettingsButton().isEnabled = enabled
        findGameModesButton().isEnabled = enabled
    }

    private fun findMoreGamesButton() = findViewById<MoreGamesButton>(R.id.more_games_button)

    private fun findNoAdsButton() = findViewById<NoAdsButton>(R.id.no_ads_button)

    private fun findRateAppButton() = findViewById<RateAppButton>(R.id.rate_app_button)

    private fun findSettingsButton() = findViewById<SettingsButton>(R.id.settings_button)

    private fun findGameModesButton() = findViewById<GameModesButton>(R.id.game_modes_button)
}
