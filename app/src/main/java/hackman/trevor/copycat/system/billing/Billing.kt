package hackman.trevor.copycat.system.billing

import androidx.lifecycle.lifecycleScope
import com.android.billingclient.api.*
import com.android.billingclient.api.Purchase.PurchasesResult
import hackman.trevor.copycat.MainActivity
import hackman.trevor.copycat.system.*
import hackman.trevor.copycat.ui.DialogFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.annotations.TestOnly

/**
 * Handles all things related to billing for in-app purchases.
 * There's two major reasons why billing may not work. No network connection or Google Play Store (required) not installed/force stopped/disabled/updating.
 * Only returning a network error dialog for now. Could improve in the future to detect if Google Play Store is installed and return a different error dialog for that. But I expect 99% of app-users have Google Play.
 * Google documentation (garbage documentation) : https://developer.android.com/google/play/billing/billing_library_overview
 */
class BillingManager(private val mainActivity: MainActivity) {

    private val billingClient = BillingClient.newBuilder(mainActivity).enablePendingPurchases()
        .setListener(PurchaseListener())
        .build().apply { startConnection(BillingStateListener()) }

    private var reconnectJob: Job? = null

    /**
     * Call to present Google's purchase dialog
     * User may make the purchase, cancel, or fail to make purchase
     */
    fun startPurchaseFlow() {
        val skuList = listOf(NO_ADS)
        val skuDetailsParams = SkuDetailsParams.newBuilder()
            .setSkusList(skuList)
            .setType(BillingClient.SkuType.INAPP)
            .build()

        billingClient.querySkuDetailsAsync(skuDetailsParams, SkuRetrievalListener())
    }

    private fun acknowledgePurchase(purchase: Purchase) {
        val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()
        billingClient.acknowledgePurchase(acknowledgePurchaseParams, AcknowledgePurchaseListener())
    }

    private inner class SkuRetrievalListener : SkuDetailsResponseListener {
        override fun onSkuDetailsResponse(
            billingResult: BillingResult?,
            skuDetails: List<SkuDetails>?
        ) {
            when {
                billingResult.isNull() -> report("Sku: Null billing result")
                skuDetails.isNullOrEmpty() -> onSkuError()
                successfulSkuRetrieval(billingResult) -> onSuccessfulSkuRetrieval(skuDetails)
                else -> onFailedSkuRetrieval(billingResult)
            }
        }

        private fun onSkuError() {
            report("This shouldn't happen. Null details on OK response : " + billingClient.isReady)
            DialogFactory(mainActivity).failedNetwork().show()
        }

        private fun successfulSkuRetrieval(billingResult: BillingResult) =
            billingResult.responseCode == BillingClient.BillingResponseCode.OK

        private fun onSuccessfulSkuRetrieval(skuDetails: List<SkuDetails>) {
            for (skuDetail in skuDetails) {
                when (skuDetail.sku) {
                    NO_ADS -> onRetrievedNoAds(skuDetail)
                    else -> onUnknownSku(skuDetail)
                }
            }
        }

        private fun onRetrievedNoAds(skuDetail: SkuDetails) {
            val billingFlowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(skuDetail)
                .build()
            billingClient.launchBillingFlow(mainActivity, billingFlowParams)
        }

        private fun onUnknownSku(skuDetail: SkuDetails) =
            report(
                "This shouldn't happen. Unknown querySkuDetailsAsync result?: " +
                        skuDetail.sku + " : " + skuDetail
            )


        private fun onFailedSkuRetrieval(billingResult: BillingResult) {
            flog(
                "querySkuDetailsAsync failed : " +
                        billingResponseToName(billingResult) +
                        " : " + billingResult.debugMessage + " : " + billingClient.isReady
            )
            DialogFactory(mainActivity).failedNetwork().show()
        }
    }

    private inner class AcknowledgePurchaseListener : AcknowledgePurchaseResponseListener {
        override fun onAcknowledgePurchaseResponse(billingResult: BillingResult?) =
            when {
                billingResult.isNull() -> report("Acknowledge: Null billing result")
                successfullyAcknowledged(billingResult) -> onSuccessfulAcknowledgement()
                else -> onFailedToAcknowledge(billingResult)
            }

        private fun successfullyAcknowledged(billingResult: BillingResult) =
            billingResult.responseCode == BillingClient.BillingResponseCode.OK

        private fun onSuccessfulAcknowledgement() {
            flog("Successfully acknowledged")
            DialogFactory(mainActivity).successfulNoAdsPurchase().show()
        }

        // I hope this doesn't happen. Not sure what to do since I believe user already paid, but acknowledgement is required
        // Failure to acknowledge within 3 days results in purchases being refunded
        private fun onFailedToAcknowledge(billingResult: BillingResult) =
            report(
                "Failed to acknowledge : " + billingResponseToName(
                    billingResult
                ) + " : " + billingResult.responseCode
            )
    }

    private inner class PurchaseListener : PurchasesUpdatedListener {
        override fun onPurchasesUpdated(
            billingResult: BillingResult?,
            purchases: List<Purchase>?
        ) {
            when {
                billingResult.isNull() -> report("Purchase: Null billing result")
                userCancelledPurchase(billingResult) -> onUserCancelledPurchase()
                purchases.isNullOrEmpty() -> onPurchasesError()
                successfullyPurchased(billingResult) -> onSuccessfulPurchase(purchases)
                else -> onFailedToMakePurchase(billingResult)
            }
        }

        private fun onPurchasesError() {
            report("This shouldn't happen. Null purchases on OK response : " + billingClient.isReady)
            DialogFactory(mainActivity).unknownError("Sorry, there was an error finding your purchase")
                .show()
        }

        private fun successfullyPurchased(billingResult: BillingResult) =
            billingResult.responseCode == BillingClient.BillingResponseCode.OK

        private fun onSuccessfulPurchase(purchases: List<Purchase>) {
            flog("Purchase made : $purchases")

            // Not sure why there would be more than one purchase updated at a time, but to be thorough we'll loop through the whole list
            for (purchase in purchases) {
                when (purchase.sku) {
                    NO_ADS -> onNoAdsPurchased()
                    else -> onUnknownPurchase(purchase)
                }
            }
        }

        private fun onNoAdsPurchased() {
            SaveData.getInstance(mainActivity).isNoAdsOwned = true
        }

        private fun onUnknownPurchase(purchase: Purchase) {
            report("This shouldn't happen. Unknown purchase? : " + purchase.sku + " : " + purchase.purchaseToken)
            DialogFactory(mainActivity).unknownError("Sorry, unknown item purchased")
        }

        private fun userCancelledPurchase(billingResult: BillingResult) =
            billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED

        private fun onUserCancelledPurchase() = flog("User cancelled purchase")

        private fun onFailedToMakePurchase(billingResult: BillingResult) {
            report(
                "Unsuccessful purchase : " +
                        billingResponseToName(billingResult) +
                        " : " + billingResult.debugMessage
            )
            DialogFactory(mainActivity).failedNetwork()
        }
    }

    /**
     * This is NOT a network connection listener. Can return OK when no connection.
     * Connects with Google Play Store installed on device.
     */
    private inner class BillingStateListener : BillingClientStateListener {

        override fun onBillingSetupFinished(billingResult: BillingResult?) =
            when {
                billingResult.isNull() -> report("State: Null billing result")
                successfullyConnected(billingResult) -> onSuccessfulConnection()
                else -> onFailedConnection(billingResult)
            }

        private fun successfullyConnected(billingResult: BillingResult) =
            billingResult.responseCode == BillingClient.BillingResponseCode.OK

        private fun onSuccessfulConnection() {
            flog("startConnection succeeded")
            reconnectJob?.cancel()
            queryPurchases()
        }

        private fun onFailedConnection(billingResult: BillingResult) {
            flog(
                "startConnection failed : " +
                        billingResponseToName(billingResult) +
                        " : " + billingResult.debugMessage
            )
            attemptReconnect()
        }

        // Called if Google Play Store is force stopped or otherwise no longer running
        override fun onBillingServiceDisconnected() {
            flog("Billing Service Disconnected")
            attemptReconnect()
        }

        /**
         * Periodically attempt to reconnect on a 30 second delay
         * Must cancel reconnection attempt on reconnect
         */
        private fun attemptReconnect() {
            reconnectJob = mainActivity.lifecycleScope.launch {
                delay(30000)
                flog("Attempting to reconnect")
                billingClient.startConnection(this@BillingStateListener)
            }
        }

        // Make a non-network check to Google Play Store's cache for what purchases have been made
        private fun queryPurchases() {
            val purchasesResult: PurchasesResult =
                billingClient.queryPurchases(BillingClient.SkuType.INAPP)

            val purchases: List<Purchase>? = purchasesResult.purchasesList.filterNotNull()

            when {
                purchases.isNull() -> onNullPurchases(purchasesResult)
                purchases.isEmpty() -> onEmptyPurchases()
                else -> onSuccessfulPurchaseQuery(purchases)
            }
        }

        private fun onNullPurchases(purchasesResult: PurchasesResult) =
            report(
                "Why are purchases null? : " +
                        billingResponseToName(purchasesResult) +
                        " " + purchasesResult.billingResult.debugMessage
            )

        private fun onEmptyPurchases() = flog("No purchases owned")

        private fun onSuccessfulPurchaseQuery(purchases: List<Purchase>) {
            for (purchase in purchases) {
                if (purchase.sku == NO_ADS) onFoundNoAdsPurchased(purchase)
                else onFoundUnknownPurchase(purchase)
            }
        }

        private fun onFoundNoAdsPurchased(purchase: Purchase) {
            if (!purchase.isAcknowledged) acknowledgePurchase(purchase)

            SaveData.getInstance(mainActivity).isNoAdsOwned = true
            flog("$NO_ADS owned")
        }

        private fun onFoundUnknownPurchase(purchase: Purchase) =
            report(
                "This shouldn't happen. Unknown queryPurchase result?: "
                        + purchase.sku + " : " + purchase.purchaseToken
            )
    }

    /**
     * Gets the name of the error code
     * https://developer.android.com/reference/com/android/billingclient/api/BillingClient.BillingResponseCode
     */
    private fun billingResponseToName(billingResponseCode: Int): String {
        when (billingResponseCode) {
            0 -> return "OK"
            1 -> return "USER_CANCELLED"
            2 -> return "SERVICE_UNAVAILABLE"
            3 -> return "BILLING_UNAVAILABLE"
            4 -> return "ITEM_UNAVAILABLE"
            5 -> return "DEVELOPER_ERROR"
            6 -> return "ERROR"
            7 -> return "ITEM_ALREADY_OWNED"
            8 -> return "ITEM_NOT_OWNED"
            -1 -> return "SERVICE_DISCONNECTED"
            -2 -> return "FEATURE_NOT_SUPPORTED"
        }
        report("ERROR : Invalid response code")
        return "ERROR : Invalid response code"
    }

    // Overload
    private fun billingResponseToName(billingResult: BillingResult): String {
        return billingResponseToName(
            billingResult.responseCode
        )
    }

    // Overload
    private fun billingResponseToName(purchasesResult: PurchasesResult): String {
        return billingResponseToName(
            purchasesResult.billingResult
        )
    }

    // For testing, reset purchases.
    @TestOnly
    fun forTestingConsumeAllPurchases(main: MainActivity) {
        // Get purchases if any
        val purchases: List<Purchase> =
            billingClient.queryPurchases(BillingClient.SkuType.INAPP).purchasesList
        if (purchases.isEmpty()) log("No purchases to consume")
        else {
            for (purchase in purchases) {
                val consumeParams = ConsumeParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()
                val listener =
                    ConsumeResponseListener { billingResult, purchaseToken ->
                        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                            log("Consumed purchase successfully : $purchaseToken")
                        } else {
                            log(
                                "Consume failed : " +
                                        billingResponseToName(billingResult) +
                                        " : " + billingResult.debugMessage
                            )
                        }
                    }
                billingClient.consumeAsync(
                    consumeParams,
                    listener
                )
                log("Consuming " + purchase.sku)
            }
        }

        // Reset Keys
        SaveData.getInstance(mainActivity).isNoAdsOwned = false
    }
}
