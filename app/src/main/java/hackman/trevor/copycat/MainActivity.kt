package hackman.trevor.copycat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hackman.trevor.copycat.system.ads.AdManager
import hackman.trevor.copycat.system.log
import hackman.trevor.copycat.system.sound.SoundManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val ads = AdManager(this)
        val sounds = SoundManager(this)

        setContentView(R.layout.activity_main)
        ad_container.addView(ads.getBannerAd())

        log("Logging is working")
    }
}
