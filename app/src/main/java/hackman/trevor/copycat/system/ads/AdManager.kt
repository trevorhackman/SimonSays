package hackman.trevor.copycat.system.ads

import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import hackman.trevor.billing.Ownership
import hackman.trevor.copycat.system.SaveData
import hackman.trevor.copycat.system.log
import hackman.trevor.copycat.system.report

/**
 * Admob Ads with Firebase
 * https://firebase.google.com/docs/admob/android/quick-start
 *
 * Critical to only use test ads with test ids when testing or risk being banned
 *
 * Should only be created once and created as soon as possible on app launch
 *
 * BannerAd vs InterstitialAd
 * BannerAd has lifecycle methods. Interstitial does not.
 * Interstitial resizes on rotation. Banner does not. Should build new banner after rotation.
 * Banner has retry policy when it fails to load. Interstitial does not.
 */
object AdManager : LifecycleObserver {

    private var activity: AppCompatActivity? = null

    private var isInitialized = false

    private var bannerAd: AdView? = null

    private var interstitialAd: InterstitialAd? = null

    // Check this before showing ads
    val isEnabled
        get() = isInitialized && SaveData.isNoAdsOwned == Ownership.ConfirmedUnowned

    fun setup(activity: AppCompatActivity) {
        this.activity = activity
        interstitialAd = InterstitialAdBuilder(activity, ::requestNewInterstitialAd).build()
        isInitialized = true
        activity.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun onCreate() {
        @Suppress("DEPRECATION") // Despite deprecation, this function is recommended
        MobileAds.initialize(activity, admobId)
        requestNewInterstitialAd()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onResume() {
        bannerAd?.resume()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun onPause() {
        bannerAd?.pause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onDestroy() {
        bannerAd?.destroy()
        bannerAd = null
        interstitialAd = null
        activity = null
    }

    private fun requestNewBannerAd() {
        val adRequest = AdRequest.Builder().build()
        bannerAd?.loadAd(adRequest)
    }

    private fun requestNewInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        interstitialAd?.loadAd(adRequest)
    }

    // Get the whole banner ad in order to display it somewhere
    fun getBannerAd(): AdView {
        removeView()
        return bannerAd ?: buildBannerAd()
    }

    /**
     * Banner ad does not resize when the screen resizes
     * This method must be called to recreate & resize a banner ad when the screen size changes
     */
    fun buildBannerAd() = BannerAdBuilder(activity!!, ::requestNewBannerAd).build().also {
        removeView()
        bannerAd = it
        requestNewBannerAd()
    }

    private fun removeView() {
        bannerAd?.parent?.let { parent ->
            (parent as ViewGroup).removeView(bannerAd)
        }
    }

    /**
     * Show interstitial ad if possible
     * @param probability [0,1] chance of showing ad
     * @return whether shown
     */
    fun showInterstitialAd(probability: Double): Boolean {
        val interstitialAd = interstitialAd ?: kotlin.run {
            report("Interstitial ad is null")
            return false
        }
        return if (probability > Math.random()) {
            if (interstitialAd.isLoaded) {
                interstitialAd.show()
                true
            } else {
                log("Interstitial ad is not loaded and cannot be shown")
                if (!interstitialAd.isLoading) requestNewInterstitialAd()
                false
            }
        } else false
    }

    /**
     * The meaning of the error codes that AdMob may return
     * https://developers.google.com/android/reference/com/google/android/gms/ads/AdRequest#ERROR_CODE_INTERNAL_ERROR
     */
    fun errorCodeToString(errorCode: Int): String {
        when (errorCode) {
            0 -> return "ERROR_CODE_INTERNAL_ERROR" // Something happened internally; for instance, an invalid response was received from the ad server.
            1 -> return "ERROR_CODE_INVALID_REQUEST" // The ad request was invalid; for instance, the ad unit ID was incorrect.
            2 -> return "ERROR_CODE_NETWORK_ERROR" // The ad request was unsuccessful due to network connectivity.
            3 -> return "ERROR_CODE_NO_FILL" // The ad request was successful, but no ad was returned due to lack of ad inventory.
        }
        report("ERROR : Invalid response code")
        return "ERROR : Invalid response code"
    }
}
