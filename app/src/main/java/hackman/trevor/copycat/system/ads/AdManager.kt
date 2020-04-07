package hackman.trevor.copycat.system.ads

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import hackman.trevor.copycat.MainActivity
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
class AdManager(private val mainActivity: MainActivity) : LifecycleObserver {
    init {
        mainActivity.lifecycle.addObserver(this)
    }

    private var bannerAd: AdView? = null

    private val interstitialAd by lazy {
        InterstitialAdBuilder(mainActivity, ::requestNewInterstitialAd).build()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(owner: LifecycleOwner) {
        MobileAds.initialize(mainActivity, admobId)
        initialize()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume(owner: LifecycleOwner) {
        bannerAd?.resume()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause(owner: LifecycleOwner) {
        bannerAd?.pause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(owner: LifecycleOwner) {
        bannerAd?.destroy()
    }

    private fun initialize() {
        requestNewBannerAd()
        requestNewInterstitialAd()
    }

    private fun requestNewBannerAd() {
        val adRequest = AdRequest.Builder().build()
        bannerAd?.loadAd(adRequest)
    }

    private fun requestNewInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        interstitialAd.loadAd(adRequest)
    }

    // Get the whole banner ad in order to display it somewhere
    fun getBannerAd(): AdView {
        bannerAd?.parent?.let { parent ->
            (parent as ViewGroup).removeView(bannerAd)
        }
        return bannerAd ?: buildBannerAd()
    }

    // TODO Load new banner ad when device rotates, call in config change or only if width too large?
    fun buildBannerAd(): AdView =
        BannerAdBuilder(mainActivity, ::requestNewBannerAd).build().also {
            bannerAd = it
        }

    /**
     * Show interstitial ad if possible
     * Return whether shown
     */
    fun showInterstitialAd(): Boolean =
        if (interstitialAd.isLoaded) {
            interstitialAd.show()
            true
        } else {
            log("Interstitial ad is not loaded and cannot be shown")
            if (!interstitialAd.isLoading) requestNewInterstitialAd()
            false
        }

    companion object {
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
}