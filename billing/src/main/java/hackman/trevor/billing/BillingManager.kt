package hackman.trevor.billing

import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.android.billingclient.api.*
import hackman.trevor.billing.internal.*
import org.jetbrains.annotations.TestOnly

/**
 * Handles all things related to billing for in-app purchases.
 * There's two major reasons why billing may not work. No network connection or Google Play Store (required) not installed/force stopped/disabled/updating.
 * Google documentation (garbage documentation) : https://developer.android.com/google/play/billing/billing_library_overview
 */
object BillingManager : LifecycleObserver {

    internal var activity: FragmentActivity? = null

    internal lateinit var billingClient: BillingClient

    fun setup(activity: FragmentActivity) {
        this.activity = activity
        activity.lifecycle.addObserver(this)
        billingClient = BillingClient.newBuilder(activity).enablePendingPurchases()
            .setListener(PurchaseListener)
            .build().apply { startConnection(BillingStateListener) }
    }

    /**
     * [BillingClient.isReady] will be false if [BillingClient.startConnection] fails
     * As well as if [BillingClientStateListener.onBillingServiceDisconnected] happens
     **/
    val isReady: Boolean
        get() = billingClient.isReady

    /**
     * Call to present Google's purchase dialog
     * User may make the purchase, cancel, or fail to make purchase
     */
    fun startPurchaseFlow(skuDetail: SkuDetails) {
        if (!billingClient.isReady) {
            model.billingResponse.value = BillingResponse.BILLING_UNAVAILABLE
            return
        }

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setSkuDetails(skuDetail)
            .build()
        activity?.takeIf { !it.isFinishing }?.let {
            billingClient.launchBillingFlow(it, billingFlowParams)
        }
    }

    /**
     * Call if [querySkuDetails], which gets called when billing connects, failed and we want to retry
     */
    fun retryQuerySku() {
        SkuRetrievalListener.retrySkuRetrieval()
    }

    internal fun querySkuDetails() {
        val skuDetailsParams = SkuDetailsParams.newBuilder()
            .setSkusList(NO_ADS_LIST)
            .setType(BillingClient.SkuType.INAPP)
            .build()

        billingClient.querySkuDetailsAsync(skuDetailsParams, SkuRetrievalListener)
    }

    internal fun acknowledgePurchase(purchase: Purchase) {
        val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()
        billingClient.acknowledgePurchase(acknowledgePurchaseParams, AcknowledgeListener)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onDestroy() {
        Log.e("TT_", "TEST ON DESTROY CALLED")
        activity = null
    }

    @TestOnly // Reset all purchases for testing
    fun forTestingConsumeAllPurchases() {
        if (!billingClient.isReady) {
            model.log.value = "Billing not connected"
            return
        }

        val listener = ConsumeResponseListener { billingResult, purchaseToken ->
            model.log.value =
                if (billingResult.isSuccessful()) "Consumed purchase successfully : $purchaseToken"
                else "Consume failed : ${billingResponseToName(billingResult)} ${billingResult.debugMessage}"
        }

        // Get purchases if any
        val purchases = billingClient.queryPurchases(BillingClient.SkuType.INAPP).purchasesList
        if (purchases.isNullOrEmpty()) {
            model.log.value = "No purchases to consume"
        } else for (purchase in purchases) {
            val consumeParams = ConsumeParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()
            billingClient.consumeAsync(
                consumeParams,
                listener
            )
            model.log.value = "Consuming ${purchase.sku}"
        }

        // Reset Keys
        model.ownership.value = Ownership.Unknown
    }
}
