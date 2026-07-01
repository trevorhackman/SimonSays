package hackman.trevor.copycat.logic.remove_ads

import hackman.trevor.copycat.logic.remove_ads.Products.RemoveAds1
import hackman.trevor.copycat.logic.remove_ads.Products.RemoveAds4
import hackman.trevor.tlibrary.billing.Product

/**
 * Representation of the four purchase options for removing ads
 * From the lowest price to the highest price, [RemoveAds1] to [RemoveAds4]
 */
object Products {
    val RemoveAds1 = Product("no_ads")
    val RemoveAds2 = Product("remove_ads_2")
    val RemoveAds3 = Product("remove_ads_3")
    val RemoveAds4 = Product("remove_ads_4")

    val ALL_PRODUCTS = listOf(RemoveAds1, RemoveAds2, RemoveAds3, RemoveAds4)
}

/**
 * The display prices of the four purchase options for removing ads.
 * The strings vary by currency, obtain through a Google Billing sku call.
 * Organized by the lowest price to the highest price from [price1] to [price4].
 */
data class Prices(
    val price1: String,
    val price2: String,
    val price3: String,
    val price4: String
)