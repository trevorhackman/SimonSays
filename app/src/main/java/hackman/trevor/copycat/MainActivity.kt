package hackman.trevor.copycat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hackman.trevor.copycat.system.ads.AdManager
import hackman.trevor.copycat.system.log
import hackman.trevor.copycat.system.sound.SoundManager
import hackman.trevor.copycat.ui.main.MainFragment

class MainActivity : AppCompatActivity() {
    private val injectorViewModel: Injector by injector()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val ads = AdManager(this)
        val sounds = SoundManager(this)
        injectorViewModel.inject(ads, sounds)

        setContentView(R.layout.fragment_container)
        supportFragmentManager.beginTransaction().add(
            R.id.fragment_container,
            MainFragment()
        ).commit()

        log("Logging is working")
    }
}
