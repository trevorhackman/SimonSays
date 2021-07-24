package hackman.trevor.copycat.system.ads

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.LoadAdError
import hackman.trevor.copycat.system.TESTING
import hackman.trevor.copycat.system.interstitialAdId
import hackman.trevor.copycat.system.log
import hackman.trevor.copycat.system.testInterstitialAdId
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class InterstitialAdBuilder(
    private val activity: AppCompatActivity,
    private val requestNewInterstitialAd: () -> Unit
) {
    fun build(): InterstitialAd =
        InterstitialAd(activity).apply {
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
        override fun onAdFailedToLoad(adError: LoadAdError) {
            super.onAdFailedToLoad(adError)
            activity.lifecycleScope.launch {
                delay(60000)
                requestNewInterstitialAd()
            }
            log("Interstitial ad failed to load: $adError")
        }
    }
}
