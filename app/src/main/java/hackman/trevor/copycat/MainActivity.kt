package hackman.trevor.copycat

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hackman.trevor.copycat.system.ads.AdManager
import hackman.trevor.copycat.system.billing.BillingManager
import hackman.trevor.copycat.system.log
import hackman.trevor.copycat.system.sound.SoundManager
import hackman.trevor.copycat.ui.main.MainFragment

class MainActivity : AppCompatActivity() {
    private val activityInterface: ActivityInterface by injector()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val ads = AdManager(this)
        val sounds = SoundManager(this)
        val billing = BillingManager(this)
        activityInterface.inject(ads, sounds, billing)

        setContentView(R.layout.fragment_container)
        supportFragmentManager.beginTransaction().add(
            R.id.fragment_container,
            MainFragment()
        ).commit()

        log("Logging is working")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container,
            MainFragment()
        ).commit()
    }

    override fun onBackPressed() {
        val performSuper = activityInterface.onBackPressed?.invoke()
        if (performSuper != false) super.onBackPressed()
    }
}
