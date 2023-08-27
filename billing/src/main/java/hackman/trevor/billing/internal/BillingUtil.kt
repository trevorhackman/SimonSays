package hackman.trevor.billing.internal

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingResult
import hackman.trevor.billing.model

internal fun BillingResult.isSuccessful() = responseCode == BillingClient.BillingResponseCode.OK

internal fun BillingResult.isAlreadyOwned() = responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED

/**
 * Gets the name of the error code
 * https://developer.android.com/reference/com/android/billingclient/api/BillingClient.BillingResponseCode
 */
internal fun billingResponseToName(billingResponseCode: Int): String {
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
        12 -> return "NETWORK_ERROR"
        -1 -> return "SERVICE_DISCONNECTED"
        -2 -> return "FEATURE_NOT_SUPPORTED"
        -3 -> return "SERVICE_TIMEOUT"
    }
    model.report.value = "ERROR : Invalid response code"
    return "ERROR : Invalid response code"
}

// Overload
internal fun billingResponseToName(billingResult: BillingResult): String =
    billingResponseToName(billingResult.responseCode)
