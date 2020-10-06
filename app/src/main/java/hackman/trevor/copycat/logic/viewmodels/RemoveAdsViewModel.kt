package hackman.trevor.copycat.logic.viewmodels

import androidx.lifecycle.MutableLiveData
import hackman.trevor.billing.Billing
import hackman.trevor.billing.BillingManager
import hackman.trevor.copycat.logic.remove_ads.Prices
import hackman.trevor.copycat.logic.remove_ads.Product
import hackman.trevor.copycat.requireValue
import hackman.trevor.copycat.system.sound.SoundManager

class RemoveAdsViewModelImpl : MenuViewModel(), RemoveAdsViewModel {
    override val prices = MutableLiveData<Prices>()

    override val isReadyToShow
        get() = prices.value != null

    override fun productClicked(product: Product) {
        SoundManager.click.play()
        val skuDetails = Billing.liveData.skuDetails.requireValue()
        val skuDetail = skuDetails[Product.values().indexOf(product)]
        BillingManager.startPurchaseFlow(skuDetail)
    }

    override fun closeClicked() {
        SoundManager.click.play()
        setInBackground(true)
    }
}

interface RemoveAdsViewModel : Menu {
    val prices: MutableLiveData<Prices>

    val isReadyToShow: Boolean

    fun productClicked(product: Product)

    fun closeClicked()
}
