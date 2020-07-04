package hackman.trevor.copycat

import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hackman.trevor.copycat.system.SaveData
import hackman.trevor.copycat.system.ads.AdManager
import hackman.trevor.copycat.system.billing.BillingManager
import hackman.trevor.copycat.system.billing.Ownership
import hackman.trevor.copycat.system.log
import hackman.trevor.copycat.system.sound.SoundManager
import hackman.trevor.copycat.ui.DialogFactory
import hackman.trevor.copycat.ui.main.MainFragment

class MainActivity : AppCompatActivity() {
    private val activityInterface: ActivityInterface by injector()

    private val sounds by lazy { SoundManager(this) }
    private val billing by lazy { BillingManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSaveData()
        initDialogFactory()
        initAdsIfNotOwned()
        injectDependencies()

        setContentView(R.layout.fragment_container)

        // TODO Splash screen
        supportFragmentManager.beginTransaction().add(
            R.id.fragment_container,
            MainFragment()
        ).commit()

        log("Logging is working")
    }

    private fun injectDependencies() = activityInterface.inject(sounds, billing)

    private fun initSaveData() = SaveData.setup(applicationContext as Application)

    private fun initDialogFactory() = DialogFactory.setup(this)

    private fun initAdsIfNotOwned() {
        if (SaveData.isNoAdsOwned != Ownership.Owned) AdManager.setup(this)
    }

    override fun onBackPressed() {
        val shouldPerformSuper = activityInterface.onBackPressed?.invoke()
        if (shouldPerformSuper != false) super.onBackPressed()
    }
}
