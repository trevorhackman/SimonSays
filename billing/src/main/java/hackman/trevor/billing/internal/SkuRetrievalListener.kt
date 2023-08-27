package hackman.trevor.billing.internal

import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.SkuDetails
import com.android.billingclient.api.SkuDetailsResponseListener
import hackman.trevor.billing.BillingManager
import hackman.trevor.billing.NO_ADS_LIST
import hackman.trevor.billing.model

// To prepare for potential purchases, query for `Sku`s at launch of app
// Important to get prices to display which will vary by currency
internal object SkuRetrievalListener : SkuDetailsResponseListener {

    private var isRetry = false

    fun retrySkuRetrieval() {
        isRetry = true
        BillingManager.querySkuDetails()
    }

    override fun onSkuDetailsResponse(billingResult: BillingResult, skuDetails: List<SkuDetails>?) {
        when {
            skuDetails.isNullOrEmpty() -> onSkuError(billingResult)
            billingResult.isSuccessful() -> onSkuRetrieval(skuDetails)
            else -> onFailedSkuRetrieval(billingResult)
        }
    }

    // This can happen if billing is not connected
    private fun onSkuError(billingResult: BillingResult) {
        model.report.value = "Null details on OK response : ${billingResponseToName(billingResult)} ${billingResult.debugMessage} ${BillingManager.isReady}"
        onRetryFailure()
    }

    private fun onSkuRetrieval(skuDetails: List<SkuDetails>) {
        val isExpected = skuDetails.count() == 4 && skuDetails.all {
            NO_ADS_LIST.contains(it.sku)
        }
        if (isExpected) onRetrievedNoAds(skuDetails)
        else onUnexpectedResult(skuDetails)
    }

    private fun onRetrievedNoAds(skuDetails: List<SkuDetails>) {
        model.skuDetails.value = skuDetails
        onRetrySuccess()
    }

    private fun onUnexpectedResult(skuDetails: List<SkuDetails>) {
        model.report.value = "This shouldn't happen. Unexpected querySkuDetailsAsync result: ${skuDetails.map { it.sku }} $skuDetails"
        model.skuDetails.value = null
        onRetryFailure()
    }

    private fun onFailedSkuRetrieval(billingResult: BillingResult) {
        model.flog.value = "querySkuDetailsAsync failed : ${billingResponseToName(billingResult)} ${billingResult.debugMessage} ${BillingManager.isReady}"
        model.skuDetails.value = null
        onRetryFailure()
    }

    private fun onRetrySuccess() {
        if (isRetry) {
            model.retrySkuRetrievalSuccess.value = true
            isRetry = false
        }
    }

    private fun onRetryFailure() {
        if (isRetry) {
            model.retrySkuRetrievalSuccess.value = false
            isRetry = false
        }
    }
}
