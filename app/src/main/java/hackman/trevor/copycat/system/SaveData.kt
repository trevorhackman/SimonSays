package hackman.trevor.copycat.system

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import hackman.trevor.copycat.logic.game.GameMode
import hackman.trevor.copycat.logic.settings.ColorSet
import hackman.trevor.copycat.logic.settings.FailureSound
import hackman.trevor.copycat.logic.settings.Speed
import hackman.trevor.copycat.system.billing.Ownership

/**
 * Wrapper object handling persistent data via [SharedPreferences]
 *
 * Using try/catch to be crash/free from class cast exceptions that occur when the value we read is an unexpected type
 */
object SaveData {

    private lateinit var application: Application

    fun setup(application: Application) {
        this.application = application
    }

    private val preferences by lazy {
        application.getSharedPreferences("default", Context.MODE_PRIVATE)
    }

    private val editor by lazy {
        preferences.edit()
    }

    // Whether or not noAds has been purchased. Only show ads if not owned
    var isNoAdsOwned: Ownership
        get() = Ownership.values()[preferences.safeGetInt(isNoAdsOwnedKey, Ownership.Unknown.ordinal)]
        set(value) = set(isNoAdsOwnedKey, value.ordinal)

    // Remember the game speed selected in the settings
    var speed: Speed
        get() = Speed.values()[preferences.safeGetInt(speedKey, Speed.Normal.ordinal)]
        set(value) = set(speedKey, value.ordinal)

    // Remember the color theme selected in the settings
    var colorSet: ColorSet
        get() = ColorSet.values()[preferences.safeGetInt(colorSetKey, ColorSet.Classic.ordinal)]
        set(value) = set(colorSetKey, value.ordinal)

    // Remember the failure sound selected in the settings
    var failureSound: FailureSound
        get() = FailureSound.values()[preferences.safeGetInt(failureSoundKey, FailureSound.ClassicError.ordinal)]
        set(value) = set(failureSoundKey, value.ordinal)

    // Whether or not rating dialog has been displayed before
    var isRatingRequestDisplayed: Boolean
        get() = preferences.safeGetBoolean(isRatingRequestDisplayedKey, false)
        set(value) = set(isRatingRequestDisplayedKey, value)

    // Remember the game mode last selected
    var gameMode: GameMode
        get() = GameMode.values()[preferences.safeGetInt(gameModeKey, GameMode.Classic.ordinal)]
        set(value) = set(gameModeKey, value.ordinal)

    // Track the number of games completed
    var gamesCompleted: Int
        get() = preferences.safeGetInt(gamesCompletedKey, 0)
        set(value) = set(gamesCompletedKey, value)

    // Get the highscore of the respective gameMode
    fun getHighScore(gameMode: GameMode) = preferences.safeGetInt(gameMode.name + modeBestKey, 0)

    // Save a different highscore for each game mode
    fun saveHighScore(gameMode: GameMode, highscore: Int) = set(gameMode.name + modeBestKey, highscore)

    private fun set(name: String, value: Boolean) {
        editor.putBoolean(name, value)
        editor.apply()
    }

    private fun set(name: String, value: Int) {
        editor.putInt(name, value)
        editor.apply()
    }

    private fun SharedPreferences.safeGetBoolean(key: String, default: Boolean) = try {
        getBoolean(key, default)
    } catch (e: ClassCastException) {
        flog("Caught saved value that wasn't a boolean")
        default
    }

    private fun SharedPreferences.safeGetInt(key: String, default: Int) = try {
        getInt(key, default)
    } catch (e: ClassCastException) {
        flog("Caught saved value that wasn't an integer")
        default
    }

    private const val isNoAdsOwnedKey = "noAdsOwned"
    private const val isRatingRequestDisplayedKey = "ratingRequestDisplayed"
    private const val gamesCompletedKey = "gamesCompleted"
    private const val speedKey = "speed"
    private const val colorSetKey = "colors"
    private const val failureSoundKey = "failureSound"
    private const val gameModeKey = "gameMode"
    private const val modeBestKey = "Best"
}
