package hackman.trevor.billing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.billingclient.api.SkuDetails

internal lateinit var model: BillingDataImpl

object Billing {
    lateinit var liveData: BillingData
        private set

    fun create() {
        model = BillingDataImpl()
        liveData = model
    }
}

interface BillingData {
    val skuDetails: LiveData<List<SkuDetails>>

    val retrySkuRetrievalSuccess: LiveData<Boolean>

    val billingResponse: LiveData<BillingResponse>

    val ownership: LiveData<Ownership>
    
    val log: LiveData<String>
    
    val flog: LiveData<String>
    
    val report: LiveData<String>
}

class BillingDataImpl : BillingData {
    override val skuDetails = MutableLiveData<List<SkuDetails>>()

    override val retrySkuRetrievalSuccess = MutableLiveData<Boolean>()

    override val billingResponse = MutableLiveData<BillingResponse>()

    override val ownership = MutableLiveData<Ownership>()

    override val log = MutableLiveData<String>()

    override val flog = MutableLiveData<String>()

    override val report = MutableLiveData<String>()
}

enum class BillingResponse {
    SUCCESSFUL_PURCHASE,
    BILLING_UNAVAILABLE,
    NETWORK_ERROR,
    UNKNOWN_ERROR;

    var errorMessage: String = ""
        internal set
}
