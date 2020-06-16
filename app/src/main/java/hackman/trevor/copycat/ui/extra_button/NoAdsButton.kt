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

class NoAdsButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : ExtraButton(context, attributeSet) {

    private lateinit var soundManager: SoundManager
    private lateinit var billingManager: BillingManager

    private val noAdsDialog by lazy {
        DialogFactory(context).purchaseMenu {
            billingManager.startPurchaseFlow()
        }
    }

    private val noAdsAlreadyPurchased by lazy {
        DialogFactory(context).noAdsAlreadyPurchased()
    }

    init {
        setBackground()
        setOnClickListener()
    }

    private fun setBackground() {
        background = getDrawable(R.drawable.no_ads)
    }

    private fun setOnClickListener() =
        setOnClickListener {
            soundManager.click.play()
            if (SaveData(context).isNoAdsOwned == Ownership.Owned) noAdsAlreadyPurchased.show()
            else noAdsDialog.show()
        }

    fun setup(soundManager: SoundManager, billingManager: BillingManager) {
        this.soundManager = soundManager
        this.billingManager = billingManager
    }
}
