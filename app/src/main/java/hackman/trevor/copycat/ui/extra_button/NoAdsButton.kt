package hackman.trevor.copycat.ui.extra_button

import android.content.Context
import android.util.AttributeSet
import hackman.trevor.billing.BillingManager
import hackman.trevor.billing.Ownership
import hackman.trevor.copycat.R
import hackman.trevor.copycat.logic.viewmodels.RemoveAdsViewModel
import hackman.trevor.copycat.system.SaveData
import hackman.trevor.copycat.system.getDrawable
import hackman.trevor.copycat.system.sound.SoundManager
import hackman.trevor.copycat.ui.DialogFactory
import hackman.trevor.copycat.ui.showCorrectly

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
            SaveData.isNoAdsOwned == Ownership.Owned -> noAdsAlreadyPurchased.showCorrectly()
            !BillingManager.isReady -> billingUnavailable.showCorrectly()
            !removeAdsViewModel.isReadyToShow -> BillingManager.retryQuerySku()
            else -> removeAdsViewModel.setInBackground(false)
        }
    }
}
