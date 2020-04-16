package hackman.trevor.copycat.ui.main.extra_button

import android.content.Context
import android.util.AttributeSet
import hackman.trevor.copycat.R
import hackman.trevor.copycat.system.billing.BillingManager
import hackman.trevor.copycat.system.getDrawable
import hackman.trevor.copycat.system.sound.SoundManager
import hackman.trevor.copycat.ui.DialogFactory

class RemoveAdsButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : ExtraButton(context, attributeSet) {

    private lateinit var soundManager: SoundManager
    private lateinit var billingManager: BillingManager

    private val removeAdsDialog by lazy {
        DialogFactory(context).purchaseMenu {
            billingManager.startPurchaseFlow()
        }
    }

    init {
        background = getDrawable(R.drawable.no_ads)
        setOnClickListener {
            soundManager.click.play()
            removeAdsDialog.show()
        }
    }

    fun setup(soundManager: SoundManager, billingManager: BillingManager) {
        this.soundManager = soundManager
        this.billingManager = billingManager
    }
}
