package hackman.trevor.billing.internal

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import hackman.trevor.billing.BillingManager
import hackman.trevor.billing.BillingResponse
import hackman.trevor.billing.NO_ADS_LIST
import hackman.trevor.billing.Ownership
import hackman.trevor.billing.model

internal object PurchaseListener : PurchasesUpdatedListener {

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        when {
            userCancelledPurchase(billingResult) -> onUserCancelledPurchase()
            noInternetConnection(billingResult) -> onNoInternetConnection()
            purchases.isNullOrEmpty() -> onPurchaseError(billingResult)
            billingResult.isSuccessful() -> onSuccessfulPurchase(purchases)
            else -> onFailedToMakePurchase(billingResult)
        }
    }

    private fun userCancelledPurchase(billingResult: BillingResult) =
        billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED

    private fun onUserCancelledPurchase() {
        model.flog.value = "User cancelled purchase"
    }

    private fun noInternetConnection(billingResult: BillingResult) =
        billingResult.responseCode == BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE

    private fun onNoInternetConnection() {
        model.flog.value = "No internet connection"
    }

    // This can happen if already purchased
    private fun onPurchaseError(billingResult: BillingResult) {
        model.report.value =
            "Unexpected failure to make purchase ${BillingManager.isReady} ${billingResponseToName(billingResult)} ${billingResult.debugMessage}"
        model.billingResponse.value = if (billingResult.isAlreadyOwned()) {
            BillingResponse.UNKNOWN_ERROR.apply {
                errorMessage = "Item already owned"
            }
        } else {
            BillingResponse.UNKNOWN_ERROR.apply {
                errorMessage = "Sorry, there was an error with your purchase"
            }
        }
    }

    private fun onSuccessfulPurchase(purchases: List<Purchase>) {
        model.flog.value = "Purchase made : $purchases"

        // Not sure why there would be more than one purchase updated at a time, but to be thorough we'll loop through the whole list
        purchases.forEach { purchase ->
            var recognizePurchase = false
            purchase.products.forEach { product ->
                if (NO_ADS_LIST.contains(product)) {
                    onNoAdsPurchased()
                    recognizePurchase = true
                }
            }
            if (!recognizePurchase) onUnknownPurchase(purchase)
            if (!purchase.isAcknowledged) BillingManager.acknowledgePurchase(purchase)
        }
    }

    private fun onNoAdsPurchased() {
        model.ownership.value = Ownership.Owned
        model.billingResponse.value = BillingResponse.SUCCESSFUL_PURCHASE
    }

    private fun onUnknownPurchase(purchase: Purchase) {
        model.report.value = "This shouldn't happen. Unknown purchase : ${purchase.products} ${purchase.purchaseToken}"
        model.billingResponse.value = BillingResponse.UNKNOWN_ERROR.apply {
            errorMessage = "Sorry, unknown item purchased"
        }
    }

    private fun onFailedToMakePurchase(billingResult: BillingResult) {
        model.report.value =
            "Unsuccessful purchase : ${billingResponseToName(billingResult)} ${billingResult.debugMessage}"
        model.billingResponse.value = BillingResponse.NETWORK_ERROR
    }
}
