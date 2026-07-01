package hackman.trevor.copycat.ui.remove_ads

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import hackman.trevor.copycat.R
import hackman.trevor.copycat.logic.viewmodels.RemoveAdsViewModel
import hackman.trevor.copycat.observe
import hackman.trevor.copycat.system.displayMinimum
import hackman.trevor.copycat.system.getString
import hackman.trevor.copycat.system.pixelTextSize
import hackman.trevor.tlibrary.billing.ProductDetailsBillingResponse

class RemoveAdsDescription @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : AppCompatTextView(context, attributeSet), LifecycleOwner {

    private lateinit var removeAdsViewModel: RemoveAdsViewModel
    override lateinit var lifecycle: Lifecycle

    init {
        // Old Text
        // text = getString(R.string.remove_ads_description)

        text = getString(R.string.remove_ads_donation_description)
        pixelTextSize = displayMinimum() * .05f
    }

    fun setup(removeAdsViewModel: RemoveAdsViewModel, lifecycle: Lifecycle) {
        this.removeAdsViewModel = removeAdsViewModel
        this.lifecycle = lifecycle

        observe(removeAdsViewModel.billingViewModel.billingData.productDetailsBillingResponse) {
            text = when (it) {
                ProductDetailsBillingResponse.NetworkError -> getString(R.string.remove_ads_price_network_error)
                ProductDetailsBillingResponse.Success -> getString(R.string.remove_ads_donation_description)
                is ProductDetailsBillingResponse.UnknownError -> getString(
                    R.string.remove_ads_price_unknown_error,
                    it.errorMessage
                )
                // Default to default description absent a response.
                null -> getString(R.string.remove_ads_donation_description)
            }
        }
    }
}