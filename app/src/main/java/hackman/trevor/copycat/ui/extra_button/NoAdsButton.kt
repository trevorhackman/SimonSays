package hackman.trevor.copycat.ui.extra_button

import android.content.Context
import android.util.AttributeSet
import hackman.trevor.copycat.R
import hackman.trevor.copycat.system.SaveData
import hackman.trevor.copycat.system.billing.BillingManager
import hackman.trevor.copycat.system.billing.Ownership
import hackman.trevor.copycat.system.getDrawable
import hackman.trevor.copycat.system.sound.SoundManager
import hackman.trevor.copycat.ui.DialogFactory
import hackman.trevor.copycat.ui.showCorrectly

class NoAdsButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : ExtraButton(context, attributeSet) {

    private val noAdsDialog by lazy {
        DialogFactory.purchaseMenu {
            BillingManager.startPurchaseFlow()
        }
    }

    private val noAdsAlreadyPurchased by lazy {
        DialogFactory.noAdsAlreadyPurchased()
    }

    init {
        setBackground()
        setOnClickListener()
    }

    private fun setBackground() {
        background = getDrawable(R.drawable.no_ads)
    }

    private fun setOnClickListener() = setOnClickListener {
        SoundManager.click.play()
        if (SaveData.isNoAdsOwned == Ownership.Owned) noAdsAlreadyPurchased.showCorrectly()
        else noAdsDialog.showCorrectly()
    }
}
