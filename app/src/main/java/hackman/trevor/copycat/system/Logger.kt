package hackman.trevor.copycat.system

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import java.io.PrintWriter
import java.io.StringWriter
import hackman.trevor.copycat.BuildConfig as MyBuildConfig // Important or else wrong BuildConfig is chosen by default

// Making this const can possibly introduce compilation problems if BuildConfig.DEBUG isn't const
val TESTING = MyBuildConfig.DEBUG

// Logs to logcat, uses Log.ERROR
fun log(tag: String, message: String) {
    val spacedTag = if (tag.isNotBlank()) "${getTag()} $tag" else getTag()
    if (TESTING) Log.e(spacedTag, message)
}

fun log(message: String) {
    log("", message)
}

fun log(any: Any?) {
    log(any.toString())
}

// If testing logs to logcat, else logs to Firebase
// Firebase logs are only sent if a crash occurs or a report is made
fun flog(any: Any?) {
    if (TESTING) log(any)
    else FirebaseCrashlytics.getInstance().log(any.toString())
}

// Makes a report to firebase if not testing
fun report(exception: Throwable) {
    if (TESTING) {
        // Method for getting stacktrace as string
        val stringWriter = StringWriter()
        exception.printStackTrace(PrintWriter(stringWriter)) // Writes stacktrace to sw
        val exceptionAsString = stringWriter.toString()

        log(exceptionAsString)
    } else {
        flog(exception)
        FirebaseCrashlytics.getInstance().recordException(exception)
    }
}

fun report(string: String?) {
    report(Exception(string))
}

fun report(exception: Throwable, message: String?) {
    flog(message)
    report(exception)
}

private var charTracker = 0 // For getTag

private fun getTag() = "TT_" + intToExcelName(++charTracker)
