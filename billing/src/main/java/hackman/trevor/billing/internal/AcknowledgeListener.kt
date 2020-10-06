package hackman.trevor.billing.internal

import com.android.billingclient.api.AcknowledgePurchaseResponseListener
import com.android.billingclient.api.BillingResult
import hackman.trevor.billing.model

internal object AcknowledgeListener: AcknowledgePurchaseResponseListener {
    override fun onAcknowledgePurchaseResponse(billingResult: BillingResult) {
        if (billingResult.isSuccessful()) onSuccessfulAcknowledgement()
        else onFailedToAcknowledge(billingResult)
    }

    private fun onSuccessfulAcknowledgement() {
        model.flog.value = "Successfully acknowledged"
    }

    // I hope this doesn't happen. Not sure what to do since I believe user already paid, but acknowledgement is required
    // Failure to acknowledge within 3 days results in purchases being refunded
    private fun onFailedToAcknowledge(billingResult: BillingResult) {
        model.report.value = "Failed to acknowledge : ${billingResponseToName(billingResult)} ${billingResult.responseCode}"
    }
}
