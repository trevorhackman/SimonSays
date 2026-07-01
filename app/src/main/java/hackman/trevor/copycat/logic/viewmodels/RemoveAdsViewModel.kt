package hackman.trevor.copycat.logic.viewmodels

import hackman.trevor.copycat.logic.remove_ads.Prices
import hackman.trevor.copycat.logic.remove_ads.Products
import hackman.trevor.copycat.system.SaveData
import hackman.trevor.copycat.system.sound.SoundManager
import hackman.trevor.tlibrary.billing.BillingViewModel
import hackman.trevor.tlibrary.billing.BillingViewModelImpl
import hackman.trevor.tlibrary.billing.Product
import hackman.trevor.tlibrary.observe.MutableObservable

class RemoveAdsViewModelImpl : MenuViewModel(), RemoveAdsViewModel {

    override val billingViewModel = BillingViewModelImpl().apply {
        onAppStart(SaveData.isNoAdsOwned, Products.ALL_PRODUCTS)
    }

    override val prices = MutableObservable<Prices?>(null)

    override val isReadyToShowPrices
        get() = prices.value != null

    override fun productClicked(product: Product) {
        SoundManager.click.play()
        billingViewModel.startPurchaseFlow(product)
    }

    override fun closeClicked() {
        SoundManager.click.play()
        setInBackground(true)
    }
}

interface RemoveAdsViewModel : Menu {

    val billingViewModel: BillingViewModel

    val prices: MutableObservable<Prices?>

    val isReadyToShowPrices: Boolean

    fun productClicked(product: Product)

    fun closeClicked()
}
