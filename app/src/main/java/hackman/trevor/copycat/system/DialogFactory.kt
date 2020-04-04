package hackman.trevor.copycat.system

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import hackman.trevor.copycat.R

class DialogFactory(private val context: Context) {

    private fun buildStyle() =
        AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert)

    fun leaveCurrentGame(onExit: () -> Unit, onMainMenu: () -> Unit): AlertDialog =
        buildStyle()
            .setTitle(R.string.dialog_exit_title)
            .setMessage(R.string.dialog_exit_message)
            .setPositiveButton(R.string.dialog_exit_button,
                DialogInterface.OnClickListener { _, _ -> onExit() })
            .setNeutralButton(R.string.dialog_exit_main_menu_button,
                DialogInterface.OnClickListener { _, _ -> onMainMenu() })
            .setNegativeButton(R.string.dialog_cancel_button, null)
            .create()

    fun viewMoreGames(onViewMoreGames: () -> Unit): AlertDialog =
        buildStyle()
            .setTitle(R.string.dialog_more_games_title)
            .setMessage(R.string.dialog_more_games_message)
            .setPositiveButton(R.string.dialog_more_games_view_button,
                DialogInterface.OnClickListener { _, _ -> onViewMoreGames() })
            .setNegativeButton(R.string.dialog_cancel_button, null)
            .create()

    fun rateTheApp(onRate: () -> Unit): AlertDialog =
        buildStyle()
            .setTitle(R.string.dialog_rate_app_title)
            .setMessage(R.string.dialog_rate_app_message)
            .setPositiveButton(R.string.dialog_rate_app_rate_button,
                DialogInterface.OnClickListener { _, _ -> onRate() })
            .setNegativeButton(R.string.dialog_cancel_button, null)
            .create()

    fun purchaseMenu(onPurchase: () -> Unit): AlertDialog =
        buildStyle()
            .setTitle(R.string.dialog_no_ads_title)
            .setMessage(R.string.dialog_no_ads_message)
            .setPositiveButton(R.string.dialog_no_ads_button,
                DialogInterface.OnClickListener { _, _ -> onPurchase() })
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

    fun unknownError(errorMessage: String): AlertDialog =
        buildStyle()
            .setTitle(R.string.dialog_unknown_error_title)
            .setMessage(errorMessage)
            .setNeutralButton(R.string.dialog_ok_button, null)
            .create()
}
