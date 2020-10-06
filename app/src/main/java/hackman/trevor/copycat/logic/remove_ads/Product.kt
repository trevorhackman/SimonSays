package hackman.trevor.copycat.logic.remove_ads

/**
 * Representation of the four purchase options for removing ads
 * From lowest price to highest price, [RemoveAds1] to [RemoveAds4]
 */
enum class Product {
    RemoveAds1,
    RemoveAds2,
    RemoveAds3,
    RemoveAds4,
}

/**
 * The display prices of the four purchase options for removing ads
 * The strings vary by currency, obtain through a Google Billing sku call
 * Organized lowest price to highest price from [price1] to [price4]
 */
data class Prices(
    val price1: String,
    val price2: String,
    val price3: String,
    val price4: String
)
