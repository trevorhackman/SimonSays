package hackman.trevor.copycat.system

import android.content.Context
import android.content.Intent
import android.net.Uri

class RateTheApp private constructor(private val context: Context) {
    companion object {
        fun startRateAppIntent(context: Context) {
            RateTheApp(context).startRateAppIntent()
        }
    }

    private val packageName = context.packageName
    private val marketLink = "market://details?id=$packageName"
    private val webLink = "https://play.google.com/store/apps/details?id=$packageName"

    private val marketIntent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(marketLink)
    }

    private val webIntent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(webLink)
    }

    private fun startRateAppIntent() =
        when {
            canOpenWithGooglePlay() -> openWithGooglePlay()
            canOpenWithWebBrowser() -> openWithWebBrowser()
            else -> openEmptyChooser()
        }

    private fun canOpenWithGooglePlay() =
        marketIntent.resolveActivity(context.packageManager) != null

    private fun openWithGooglePlay() = context.startActivity(marketIntent)

    private fun canOpenWithWebBrowser() = webIntent.resolveActivity(context.packageManager) != null

    private fun openWithWebBrowser() = context.startActivity(webIntent)

    /**
     * In the extremely rare case where a phone cannot open a market or web link,
     * Should create an empty chooser with message "No apps can perform this action"
     */
    private fun openEmptyChooser() = context.startActivity(Intent.createChooser(webIntent, null))
}
