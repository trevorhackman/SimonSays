package hackman.trevor.copycat

import android.app.Application
import hackman.trevor.copycat.system.flog

class SimonApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        flog("Application onCreate")
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        flog("Trim memory level $level")
    }
}
