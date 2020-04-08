package hackman.trevor.copycat.system

import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * Show a page full of my other apps in the Google Play Store
 * Attempts to open in the market (Google Play Store)
 * If not possible attempts to open in a web browser
 */
class ShowMoreGames private constructor(private val context: Context) {
    companion object {
        // This will need to be adjusted if I ever change my developer name
        private const val developerName = "Hackman"
        private const val marketLink = "market://search?q=pub:$developerName"
        private const val webLink =
            "https://play.google.com/store/search?q=pub:$developerName&c=apps"

        private val marketIntent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(marketLink)
        }

        private val webIntent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(webLink)
        }

        fun startMoreGamesIntent(context: Context) = ShowMoreGames(context).startMoreGamesIntent()
    }

    private fun startMoreGamesIntent() =
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
