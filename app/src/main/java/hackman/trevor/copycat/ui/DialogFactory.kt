package hackman.trevor.copycat.ui

import android.app.Activity
import android.app.AlertDialog
import android.app.AlertDialog.THEME_HOLO_DARK
import android.view.WindowManager
import hackman.trevor.copycat.R

object DialogFactory {

    private var activity: Activity? = null

    fun setup(activity: Activity) {
        this.activity = activity
    }

    fun dispose() {
        activity = null
    }

    @Suppress("DEPRECATION") // THEME_HOLO_DARK is dank
    private fun buildStyle() = AlertDialog.Builder(activity, THEME_HOLO_DARK)

    fun leaveCurrentGame(onExit: () -> Unit, onMainMenu: () -> Unit): AlertDialog =
        buildStyle()
            .setTitle(R.string.dialog_exit_title)
            .setMessage(R.string.dialog_exit_message)
            .setPositiveButton(R.string.dialog_exit_button) { _, _ -> onExit() }
            .setNeutralButton(R.string.dialog_exit_main_menu_button) { _, _ -> onMainMenu() }
            .setNegativeButton(R.string.dialog_cancel_button, null)
            .create()

    fun viewMoreGames(onViewMoreGames: () -> Unit): AlertDialog =
        buildStyle()
            .setTitle(R.string.dialog_more_games_title)
            .setMessage(R.string.dialog_more_games_message)
            .setPositiveButton(R.string.dialog_more_games_view_button) { _, _ -> onViewMoreGames() }
            .setNegativeButton(R.string.dialog_cancel_button, null)
            .create()

    fun rateTheApp(onRate: () -> Unit): AlertDialog =
        buildStyle()
            .setTitle(R.string.dialog_rate_app_title)
            .setMessage(R.string.dialog_rate_app_message)
            .setPositiveButton(R.string.dialog_rate_app_rate_button) { _, _ -> onRate() }
            .setNegativeButton(R.string.dialog_cancel_button, null)
            .create()

    fun purchaseMenu(onPurchase: () -> Unit): AlertDialog =
        buildStyle()
            .setTitle(R.string.dialog_no_ads_title)
            .setMessage(R.string.dialog_no_ads_message)
            .setPositiveButton(R.string.dialog_no_ads_button) { _, _ -> onPurchase() }
            .setNegativeButton(R.string.dialog_cancel_button, null)
            .create()

    fun successfulNoAdsPurchase(): AlertDialog =
        buildStyle()
            .setTitle(R.string.dialog_purchase_success_title)
            .setMessage(R.string.dialog_purchase_success_message)
            .setNeutralButton(R.string.dialog_ok_button, null)
            .create()

    fun noAdsAlreadyPurchased(): AlertDialog =
        buildStyle()
            .setTitle(R.string.dialog_already_purchased_title)
            .setMessage(R.string.dialog_already_purchased_message)
            .setNeutralButton(R.string.dialog_ok_button, null)
            .create()

    fun failedNetwork(): AlertDialog =
        buildStyle()
            .setTitle(R.string.dialog_failed_network_title)
            .setMessage(R.string.dialog_failed_network_message)
            .setNeutralButton(R.string.dialog_ok_button, null)
            .create()

    fun billingUnavailable(): AlertDialog =
        buildStyle()
            .setTitle(R.string.dialog_billing_unavailable_title)
            .setMessage(R.string.dialog_billing_unavailable_message)
            .setNeutralButton(R.string.dialog_ok_button, null)
            .create()

    fun unknownError(errorMessage: String): AlertDialog =
        buildStyle()
            .setTitle(R.string.dialog_unknown_error_title)
            .setMessage(errorMessage)
            .setNeutralButton(R.string.dialog_ok_button, null)
            .create()
}

// Don't show status bar when dialog appears. Dialogs are so overly complicated. Foogle.
fun AlertDialog.showCorrectly() {
    window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
    show()
}
