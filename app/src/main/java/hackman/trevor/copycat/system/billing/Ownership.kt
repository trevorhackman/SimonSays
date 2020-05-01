package hackman.trevor.copycat.system.billing

/**
 * Represent whether or not a billing product is owned
 * Ownership is tied to the google account not the device
 * Device does not know if [Owned] or [ConfirmedUnowned] until a successful call to the billing client
 * Default is [Unknown]
 */
enum class Ownership {
    Owned,
    ConfirmedUnowned,
    Unknown
}
