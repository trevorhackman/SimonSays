package hackman.trevor.copycat.system.ads

import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.InterstitialAd
import hackman.trevor.copycat.MainActivity
import hackman.trevor.copycat.system.TESTING
import hackman.trevor.copycat.system.log
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class InterstitialAdBuilder(
    private val mainActivity: MainActivity,
    private val requestNewInterstitialAd: () -> Unit
) {
    fun build(): InterstitialAd =
        InterstitialAd(mainActivity).apply {
            adUnitId = if (TESTING) testInterstitialAdId else interstitialAdId
            adListener = interstitialAdListener
        }

    private val interstitialAdListener = object : AdListener() {
        override fun onAdClosed() {
            super.onAdClosed()
            requestNewInterstitialAd()
        }

        override fun onAdLoaded() {
            super.onAdLoaded()
            log("Interstitial ad loaded")
        }

        /**
         * Interstitial ads don't automatically retry
         * Implement 1-minute retry policy
         */
        override fun onAdFailedToLoad(errorCode: Int) {
            super.onAdFailedToLoad(errorCode)
            mainActivity.lifecycleScope.launch {
                delay(60000)
                requestNewInterstitialAd()
            }
            log("Interstitial ad failed to load: " + AdManager.errorCodeToString(errorCode))
        }
    }
}
