package hackman.trevor.copycat

import android.app.Application
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.crashlytics.FirebaseCrashlytics
import hackman.trevor.copycat.system.*
import hackman.trevor.copycat.system.ads.AdManager
import hackman.trevor.copycat.system.sound.SoundManager
import hackman.trevor.copycat.ui.DialogFactory
import hackman.trevor.copycat.ui.main.MainFragment
import hackman.trevor.tlibrary.billing.BillingViewModelImpl
import hackman.trevor.tlibrary.billing.Ownership
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {
    private val activityInterface: ActivityInterface by activityInterface()

    override fun onCreate(savedInstanceState: Bundle?) {
        flog("Activity Created ${hashCode()}")
        super.onCreate(savedInstanceState)
        initSaveData()
        initDialogFactory()
        initAdsIfNotOwned()
        initBilling()
        initSounds()
        initCrashlytics()
        observeDoBackPressed()
        observeRequestedOrientation()

        setContentView(R.layout.fragment_container)

        // TODO Splash screen
        supportFragmentManager.beginTransaction().add(
            R.id.fragment_container,
            MainFragment()
        ).commit()
    }

    override fun onDestroy() {
        flog("Activity Destroyed ${hashCode()}")
        super.onDestroy()
    }

    private fun initSaveData() = SaveDataSetup.setup(applicationContext as Application)

    private fun initDialogFactory() = DialogFactory.setup(this)

    private fun initAdsIfNotOwned() {
        if (AdManager.IS_ENABLED && SaveData.isNoAdsOwned != Ownership.Owned) AdManager // AdManager.setup(this)
    }

    private fun initSounds() = SoundManager.setup(this)

    private fun initBilling() {
        BillingViewModelImpl.activityRef = WeakReference(this)

        // OLD, TODO remove, once new billing library has logging that can replace this
//        Billing.create()
//        Billing.liveData.apply {
//            observe(log) {
//                log(it)
//            }
//            observe(flog) {
//                flog(it)
//            }
//            observe(report) {
//                report(it)
//            }
//            observe(ownership) {
//                SaveData.isNoAdsOwned = it
//            }
//        }
//        BillingManager.setup(this)
    }

    private fun initCrashlytics() {
        // Crashlytics disabled by default, automatically enable it here if not testing
        if (!TESTING) {
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true) // Enable crashlytics
            Log.e("TT_", "Release Mode")
        } else log("TESTING")
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (activityInterface.onBackPressed?.invoke() == BackEvent.Consumed) return
        super.onBackPressed()
    }

    private fun observeDoBackPressed() = observe(activityInterface.callSuper) {
        if (it) super.onBackPressed()
    }

    private fun observeRequestedOrientation() = observe(activityInterface.requestedOrientation) {
        requestedOrientation = when (it) {
            Orientation.User -> ActivityInfo.SCREEN_ORIENTATION_USER
            Orientation.Locked -> ActivityInfo.SCREEN_ORIENTATION_LOCKED
        }
    }
}