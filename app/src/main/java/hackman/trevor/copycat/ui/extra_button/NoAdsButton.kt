package hackman.trevor.copycat.ui.extra_button

import android.content.Context
import android.util.AttributeSet
import hackman.trevor.copycat.R
import hackman.trevor.copycat.logic.viewmodels.RemoveAdsViewModel
import hackman.trevor.copycat.system.SaveData
import hackman.trevor.copycat.system.getDrawable
import hackman.trevor.copycat.system.sound.SoundManager
import hackman.trevor.copycat.ui.DialogFactory
import hackman.trevor.copycat.ui.showCorrectly
import hackman.trevor.tlibrary.billing.Ownership

class NoAdsButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : ExtraButton(context, attributeSet) {

    private lateinit var removeAdsViewModel: RemoveAdsViewModel

    private val noAdsAlreadyPurchased by lazy {
        DialogFactory.noAdsAlreadyPurchased()
    }

    private val billingUnavailable by lazy {
        DialogFactory.billingUnavailable()
    }

    private val adsAreDisabledForThisVersion by lazy {
        DialogFactory.adsAreDisabledForThisVersion()
    }

    init {
        setBackground()
        setOnClickListener()
    }

    fun setup(removeAdsViewModel: RemoveAdsViewModel) {
        this.removeAdsViewModel = removeAdsViewModel
    }

    private fun setBackground() {
        background = getDrawable(R.drawable.no_ads)
    }

    private fun setOnClickListener() = setOnClickListener {
        SoundManager.click.play()
        when {
            // Old dialog shown before I decided to allow donations.
            // !AdManager.IS_ENABLED -> adsAreDisabledForThisVersion.showCorrectly()

            // TODO, showing this dialog makes slightly less sense in the context of this game now being ad-free w/ donations.
            SaveData.isNoAdsOwned == Ownership.Owned -> noAdsAlreadyPurchased.showCorrectly()

            !removeAdsViewModel.billingViewModel.isReady -> billingUnavailable.showCorrectly()
            !removeAdsViewModel.isReadyToShowPrices -> {
                removeAdsViewModel.billingViewModel.retryFetchPrices()
                removeAdsViewModel.setInBackground(false)
            }
            else -> removeAdsViewModel.setInBackground(false)
        }
    }
}
