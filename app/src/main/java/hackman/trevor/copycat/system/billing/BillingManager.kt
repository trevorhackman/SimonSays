package hackman.trevor.copycat.system.billing

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.android.billingclient.api.*
import com.android.billingclient.api.Purchase.PurchasesResult
import hackman.trevor.copycat.system.*
import hackman.trevor.copycat.ui.DialogFactory
import hackman.trevor.copycat.ui.showCorrectly
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.annotations.TestOnly
import java.lang.ref.WeakReference

/**
 * Handles all things related to billing for in-app purchases.
 * There's two major reasons why billing may not work. No network connection or Google Play Store (required) not installed/force stopped/disabled/updating.
 * Google documentation (garbage documentation) : https://developer.android.com/google/play/billing/billing_library_overview
 */
object BillingManager {

    var isConnected = false

    private lateinit var activity: WeakReference<AppCompatActivity>

    /** Attempt to periodically reconnect if [BillingClient.startConnection] fails */
    private var reconnectJob: Job? = null

    private val billingClient by lazy {
        BillingClient.newBuilder(activity.get()!!).enablePendingPurchases()
            .setListener(purchaseListener)
            .build().apply { startConnection(billingStateListener) }
    }

    fun setup(activity: AppCompatActivity) {
        this.activity = WeakReference(activity)
        billingClient
    }

    /**
     * Call to present Google's purchase dialog
     * User may make the purchase, cancel, or fail to make purchase
     *
     * [BillingClient.isReady] will be false if [BillingClient.startConnection] fails
     */
    fun startPurchaseFlow() {
        if (!billingClient.isReady) {
            DialogFactory.billingUnavailable().showCorrectly()
            return
        }

        val skuList: List<String> = listOf(NO_ADS) // List of product IDs. Determined in Google Developer Console
        val skuDetailsParams = SkuDetailsParams.newBuilder()
            .setSkusList(skuList)
            .setType(BillingClient.SkuType.INAPP)
            .build()

        billingClient.querySkuDetailsAsync(skuDetailsParams, skuRetrievalListener)
    }

    private fun acknowledgePurchase(purchase: Purchase) {
        val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()
        billingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseListener)
    }

    //region Sku Listener

    private val skuRetrievalListener = SkuDetailsResponseListener { billingResult, skuDetails ->
        when {
            skuDetails.isNullOrEmpty() -> onSkuError()
            billingResult.isSuccessful() -> onSuccessfulSkuRetrieval(skuDetails)
            else -> onFailedSkuRetrieval(billingResult)
        }
    }

    // TODO This can happen if billing failed to connect
    private fun onSkuError() {
        report("This shouldn't happen. Null details on OK response : " + billingClient.isReady)
        DialogFactory.failedNetwork().showCorrectly()
    }

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
        billingClient.launchBillingFlow(activity.get()!!, billingFlowParams)
    }

    private fun onUnknownSku(skuDetail: SkuDetails) =
        report("This shouldn't happen. Unknown querySkuDetailsAsync result?: ${skuDetail.sku} $skuDetail")


    private fun onFailedSkuRetrieval(billingResult: BillingResult) {
        flog("querySkuDetailsAsync failed : ${billingResponseToName(billingResult)} ${billingResult.debugMessage} ${billingClient.isReady}")
        DialogFactory.failedNetwork().showCorrectly()
    }

    //endregion

    //region Purchase Listener

    private val purchaseListener = PurchasesUpdatedListener { billingResult, purchases ->
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

    private fun onUserCancelledPurchase() = flog("User cancelled purchase")

    private fun noInternetConnection(billingResult: BillingResult) =
        billingResult.responseCode == BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE

    private fun onNoInternetConnection() = flog("No internet connection")

    private fun onPurchaseError(billingResult: BillingResult) {
        report("Unexpected failure to make purchase ${billingClient.isReady} ${billingResponseToName(billingResult)} ${billingResult.debugMessage}")
        DialogFactory.unknownError("Sorry, there was an error with your purchase").showCorrectly()
    }

    private fun onSuccessfulPurchase(purchases: List<Purchase>) {
        flog("Purchase made : $purchases")

        // Not sure why there would be more than one purchase updated at a time, but to be thorough we'll loop through the whole list
        for (purchase in purchases) {
            when (purchase.sku) {
                NO_ADS -> onNoAdsPurchased()
                else -> onUnknownPurchase(purchase)
            }
            if (!purchase.isAcknowledged) acknowledgePurchase(purchase)
        }
    }

    private fun onNoAdsPurchased() {
        SaveData.isNoAdsOwned = Ownership.Owned
    }

    private fun onUnknownPurchase(purchase: Purchase) {
        report("This shouldn't happen. Unknown purchase : ${purchase.sku} ${purchase.purchaseToken}")
        DialogFactory.unknownError("Sorry, unknown item purchased")
    }

    private fun onFailedToMakePurchase(billingResult: BillingResult) {
        report("Unsuccessful purchase : ${billingResponseToName(billingResult)} ${billingResult.debugMessage}")
        DialogFactory.failedNetwork()
    }

    //endregion

    //region Acknowledge Listener

    private val acknowledgePurchaseListener = AcknowledgePurchaseResponseListener { billingResult ->
        if (billingResult.isSuccessful()) onSuccessfulAcknowledgement()
        else onFailedToAcknowledge(billingResult)
    }

    private fun onSuccessfulAcknowledgement() {
        flog("Successfully acknowledged")
        DialogFactory.successfulNoAdsPurchase().showCorrectly()
    }

    // I hope this doesn't happen. Not sure what to do since I believe user already paid, but acknowledgement is required
    // Failure to acknowledge within 3 days results in purchases being refunded
    private fun onFailedToAcknowledge(billingResult: BillingResult) =
        report("Failed to acknowledge : ${billingResponseToName(billingResult)} ${billingResult.responseCode}")

    //endregion

    //region Billing State Listener

    /**
     * This is NOT a network connection listener. Can return OK when no connection.
     * Connects with Google Play Store installed on device.
     */
    private val billingStateListener: BillingClientStateListener = object : BillingClientStateListener {

        override fun onBillingSetupFinished(billingResult: BillingResult) =
            if (billingResult.isSuccessful()) onSuccessfulConnection()
            else onFailedConnection(billingResult)

        private fun onSuccessfulConnection() {
            flog("startConnection succeeded")
            isConnected = true
            reconnectJob?.cancel()
            queryPurchases()
        }

        // Make a non-network check to Google Play Store's cache for what purchases have been made
        private fun queryPurchases() {
            val purchasesResult: PurchasesResult = billingClient.queryPurchases(BillingClient.SkuType.INAPP)

            val purchases: List<Purchase>? = purchasesResult.purchasesList?.filterNotNull()

            when {
                purchases.isNull() -> onNullPurchases(purchasesResult)
                purchases.isEmpty() -> onEmptyPurchases()
                else -> onSuccessfulPurchaseQuery(purchases)
            }
        }

        private fun onNullPurchases(purchasesResult: PurchasesResult) =
            report("Why are purchases null? : ${billingResponseToName(purchasesResult)} ${purchasesResult.billingResult.debugMessage}")

        private fun onEmptyPurchases() {
            SaveData.isNoAdsOwned = Ownership.ConfirmedUnowned
            flog("No purchases owned")
        }

        private fun onSuccessfulPurchaseQuery(purchases: List<Purchase>) {
            for (purchase in purchases) {
                if (purchase.sku == NO_ADS) onFoundNoAdsPurchased(purchase)
                else onFoundUnknownPurchase(purchase)
            }
        }

        private fun onFoundNoAdsPurchased(purchase: Purchase) {
            if (!purchase.isAcknowledged) acknowledgePurchase(purchase)

            SaveData.isNoAdsOwned = Ownership.Owned
            flog("$NO_ADS owned")
        }

        private fun onFoundUnknownPurchase(purchase: Purchase) =
            report("This shouldn't happen. Unknown queryPurchase result?: ${purchase.sku} ${purchase.purchaseToken}")

        private fun onFailedConnection(billingResult: BillingResult) {
            flog("startConnection failed ${billingResponseToName(billingResult)} ${billingResult.debugMessage}")
            isConnected = false
            attemptReconnect()
        }

        // Called if Google Play Store is force stopped or otherwise no longer running
        override fun onBillingServiceDisconnected() {
            flog("Billing Service Disconnected")
            attemptReconnect()
        }
    }

    /**
     * Periodically attempt to reconnect on a 30 second delay
     * Must cancel reconnection attempt on reconnect
     */
    private fun attemptReconnect() {
        reconnectJob = activity.get()!!.lifecycleScope.launch {
            delay(30000)
            flog("Attempting to reconnect")
            billingClient.startConnection(billingStateListener)
        }
    }

    //endregion

    private fun BillingResult.isSuccessful() = responseCode == BillingClient.BillingResponseCode.OK

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
    private fun billingResponseToName(billingResult: BillingResult): String =
        billingResponseToName(billingResult.responseCode)

    // Overload
    private fun billingResponseToName(purchasesResult: PurchasesResult): String =
        billingResponseToName(purchasesResult.billingResult)

    @TestOnly // Reset all purchases for testing
    fun forTestingConsumeAllPurchases() {
        if (!isConnected) {
            log("Billing not connected")
            return
        }

        val listener = ConsumeResponseListener { billingResult, purchaseToken ->
            if (billingResult.isSuccessful()) log("Consumed purchase successfully : $purchaseToken")
            else log("Consume failed : ${billingResponseToName(billingResult)} ${billingResult.debugMessage}")
        }

        // Get purchases if any
        val purchases = billingClient.queryPurchases(BillingClient.SkuType.INAPP).purchasesList
        if (purchases.isNullOrEmpty()) log("No purchases to consume")
        else for (purchase in purchases) {
            val consumeParams = ConsumeParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()
            billingClient.consumeAsync(
                consumeParams,
                listener
            )
            log("Consuming ${purchase.sku}")
        }

        // Reset Keys
        SaveData.isNoAdsOwned = Ownership.Unknown
    }
}
