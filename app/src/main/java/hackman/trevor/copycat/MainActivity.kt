package hackman.trevor.copycat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hackman.trevor.copycat.system.SaveData
import hackman.trevor.copycat.system.ads.AdManager
import hackman.trevor.copycat.system.billing.BillingManager
import hackman.trevor.copycat.system.billing.Ownership
import hackman.trevor.copycat.system.log
import hackman.trevor.copycat.system.sound.SoundManager
import hackman.trevor.copycat.ui.main.MainFragment

class MainActivity : AppCompatActivity() {
    private val activityInterface: ActivityInterface by injector()

    private val ads by lazy { AdManager(this) }
    private val sounds by lazy { SoundManager(this) }
    private val billing by lazy { BillingManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
        initializeAdsIfNotOwned()

        setContentView(R.layout.fragment_container)

        // TODO Splash screen
        supportFragmentManager.beginTransaction().add(
            R.id.fragment_container,
            MainFragment()
        ).commit()

        log("Logging is working")
    }

    private fun injectDependencies() = activityInterface.inject(ads, sounds, billing)

    private fun initializeAdsIfNotOwned() {
        if (SaveData(this).isNoAdsOwned != Ownership.Owned) ads.initialize()
    }

    override fun onBackPressed() {
        val shouldPerformSuper = activityInterface.onBackPressed?.invoke()
        if (shouldPerformSuper != false) super.onBackPressed()
    }
}
