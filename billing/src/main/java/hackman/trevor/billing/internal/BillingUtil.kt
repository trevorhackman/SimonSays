package hackman.trevor.billing.internal

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
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
        -1 -> return "SERVICE_DISCONNECTED"
        -2 -> return "FEATURE_NOT_SUPPORTED"
    }
    model.report.value = "ERROR : Invalid response code"
    return "ERROR : Invalid response code"
}

// Overload
internal fun billingResponseToName(billingResult: BillingResult): String =
    billingResponseToName(billingResult.responseCode)

// Overload
internal fun billingResponseToName(purchasesResult: Purchase.PurchasesResult): String =
    billingResponseToName(purchasesResult.billingResult)
