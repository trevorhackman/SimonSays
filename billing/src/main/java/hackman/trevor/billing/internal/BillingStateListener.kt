package hackman.trevor.billing.internal

import androidx.lifecycle.lifecycleScope
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.QueryPurchasesParams
import hackman.trevor.billing.BillingManager
import hackman.trevor.billing.NO_ADS_LIST
import hackman.trevor.billing.Ownership
import hackman.trevor.billing.model
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * This is NOT a network connection listener. Can return OK when no connection.
 * Connects with Google Play Store installed on device.
 */
internal object BillingStateListener : BillingClientStateListener {

    /** Attempt to periodically reconnect if [BillingClient.startConnection] fails */
    private var reconnectJob: Job? = null

    override fun onBillingSetupFinished(billingResult: BillingResult) =
        if (billingResult.isSuccessful()) onSuccessfulConnection()
        else onFailedConnection(billingResult)

    private fun onSuccessfulConnection() {
        model.flog.value = "startConnection succeeded"
        reconnectJob?.cancel()
        queryPurchases()
        BillingManager.querySkuDetails()
    }

    // Make a non-network check to Google Play Store's cache for what purchases have been made
    private fun queryPurchases() {
        BillingManager.billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.INAPP).build()
        ) { billingResult: BillingResult, purchases: List<Purchase> ->
            if (billingResult.isSuccessful()) "queryPurchases queryPurchasesAsync success : ${billingResponseToName(billingResult)}"
            else "queryPurchases queryPurchasesAsync failed : ${billingResponseToName(billingResult)}"

            when {
                purchases.isEmpty() -> onEmptyPurchases()
                else -> onSuccessfulPurchaseQuery(purchases)
            }
        }
    }

    private fun onEmptyPurchases() {
        model.ownership.value = Ownership.ConfirmedUnowned
        model.flog.value = "No purchases owned"
    }

    private fun onSuccessfulPurchaseQuery(purchases: List<Purchase>) {
        purchases.forEach { purchase ->
            var recognizePurchase = false
            purchase.products.forEach { product ->
                if (NO_ADS_LIST.contains(product)) {
                    onFoundNoAdsPurchased(purchase)
                    recognizePurchase = true
                }
            }
            if (!recognizePurchase) onFoundUnknownPurchase(purchase)
        }
    }

    private fun onFoundNoAdsPurchased(purchase: Purchase) {
        if (!purchase.isAcknowledged) BillingManager.acknowledgePurchase(purchase)

        model.ownership.value = Ownership.Owned
        model.flog.value = "${purchase.products} owned"
    }

    private fun onFoundUnknownPurchase(purchase: Purchase) {
        model.report.value =
            "This shouldn't happen. Unknown queryPurchase result?: ${purchase.products} ${purchase.purchaseToken}"
    }

    private fun onFailedConnection(billingResult: BillingResult) {
        model.flog.value =
            "startConnection failed ${billingResponseToName(billingResult)} ${billingResult.debugMessage}"
        attemptReconnect()
    }

    // Called if Google Play Store is force stopped or otherwise no longer running
    override fun onBillingServiceDisconnected() {
        model.flog.value = "Billing Service Disconnected"
        attemptReconnect()
    }

    /**
     * Periodically attempt to reconnect on a 30 second delay
     * Must cancel reconnection attempt on reconnect
     */
    private fun attemptReconnect() {
        reconnectJob = BillingManager.activity?.lifecycleScope?.launch {
            delay(30000)
            model.flog.value = "Attempting to reconnect"
            BillingManager.billingClient.startConnection(BillingStateListener)
        }
    }
}
