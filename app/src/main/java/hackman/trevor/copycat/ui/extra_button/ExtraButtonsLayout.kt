package hackman.trevor.copycat.ui.extra_button

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import hackman.trevor.copycat.R
import hackman.trevor.copycat.system.billing.BillingManager
import hackman.trevor.copycat.system.isPortrait
import hackman.trevor.copycat.system.sound.SoundManager
import hackman.trevor.copycat.ui.game_modes.GameModesViewModel
import hackman.trevor.copycat.ui.main.GameModesButton
import hackman.trevor.copycat.ui.settings.SettingsViewModel

class ExtraButtonsLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : ConstraintLayout(context, attributeSet) {

    private lateinit var soundManager: SoundManager
    private lateinit var billingManager: BillingManager
    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var gameModesViewModel: GameModesViewModel

    private var isPortrait: Boolean = isPortrait()

    init {
        inflateLayout()
    }

    private fun inflateLayout() = View.inflate(
        context,
        if (isPortrait) R.layout.extra_buttons_portrait else R.layout.extra_buttons_landscape,
        this
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (orientationChanged()) {
            updateOrientation()
            removeAllViews()
            inflateLayout()
            setupAll()
        }
    }

    private fun orientationChanged() = isPortrait != isPortrait()

    private fun updateOrientation() {
        isPortrait = isPortrait()
    }

    fun setup(
        soundManager: SoundManager,
        billingManager: BillingManager,
        settingsViewModel: SettingsViewModel,
        gameModesViewModel: GameModesViewModel
    ) {
        this.soundManager = soundManager
        this.billingManager = billingManager
        this.settingsViewModel = settingsViewModel
        this.gameModesViewModel = gameModesViewModel
        setupAll()
    }

    private fun setupAll() {
        setupMoreGamesButton()
        setupNoAdsButton()
        setupRateAppButton()
        setupSettingsButton()
        setupGameModesButton()
    }

    private fun setupMoreGamesButton() = findMoreGamesButton().setup(soundManager)

    private fun setupNoAdsButton() = findNoAdsButton().setup(soundManager, billingManager)

    private fun setupRateAppButton() = findRateAppButton().setup(soundManager)

    private fun setupSettingsButton() = findSettingsButton().setup(soundManager, settingsViewModel)

    private fun setupGameModesButton() = findGameModesButton().setup(soundManager, gameModesViewModel)

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
