package hackman.trevor.copycat.system.ads

import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import hackman.trevor.copycat.system.TESTING
import hackman.trevor.copycat.system.log

class BannerAdBuilder(
    private val activity: AppCompatActivity,
    private val requestNewBannerAd: () -> Unit
) {
    fun build(): AdView =
        AdView(activity).apply {
            adSize = AdSize.SMART_BANNER
            adUnitId = if (TESTING) testBannerAdId else bannerAdId
            adListener = bannerAdListener
        }

    private val bannerAdListener = object : AdListener() {
        override fun onAdOpened() {
            super.onAdOpened()
            requestNewBannerAd() // Get new ad to interact with after current ad
        }

        override fun onAdLoaded() {
            super.onAdLoaded()
            log("Banner ad loaded")
        }

        /**
         * There are automatic retries in 1 minute intervals for banner ads
         */
        override fun onAdFailedToLoad(errorCode: Int) {
            super.onAdFailedToLoad(errorCode)
            log("Banner ad failed to load: " + AdManager.errorCodeToString(errorCode))
        }
    }
}
