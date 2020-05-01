package hackman.trevor.copycat.system

import android.content.Context
import android.content.SharedPreferences
import hackman.trevor.copycat.logic.GameMode
import hackman.trevor.copycat.logic.settings.ColorSet
import hackman.trevor.copycat.logic.settings.Speed
import hackman.trevor.copycat.system.billing.Ownership

class SaveData private constructor(context: Context) {
    companion object {
        private var instance: SaveData? = null

        fun getInstance(context: Context): SaveData {
            if (instance == null) instance = SaveData(context)
            return instance!!
        }

        private const val isNoAdsOwnedKey = "noAdsOwned"
        private const val isRatingRequestDisplayedKey = "ratingRequestDisplayed"
        private const val gamesCompletedKey = "gamesCompleted"
        private const val speedKey = "speed"
        private const val colorSetKey = "colors"
        private const val gameModeKey = "gameMode"
        private const val modeBestKey = "Best"
    }

    private val preferences: SharedPreferences =
        context.getSharedPreferences("default", Context.MODE_PRIVATE)
    private val editor = preferences.edit()

    // Whether or not noAds has been purchased. Only show ads if not owned
    var isNoAdsOwned: Ownership
        get() = Ownership.values()[preferences.getInt(isNoAdsOwnedKey, Ownership.Unknown.ordinal)]
        set(value) = set(isNoAdsOwnedKey, value.ordinal)

    // Remember the game speed selected in the settings
    var speed: Speed
        get() = Speed.values()[preferences.getInt(speedKey, Speed.Normal.ordinal)]
        set(value) = set(speedKey, value.ordinal)

    // Remember the color theme selected in the settings
    var colorSet: ColorSet
        get() = ColorSet.values()[preferences.getInt(colorSetKey, ColorSet.Classic.ordinal)]
        set(value) = set(colorSetKey, value.ordinal)

    // Whether or not rating dialog has been displayed before
    var isRatingRequestDisplayed: Boolean
        get() = preferences.getBoolean(isRatingRequestDisplayedKey, false)
        set(value) = set(isRatingRequestDisplayedKey, value)

    // Remember the game mode last selected
    var gameMode: GameMode
        get() = GameMode.values()[preferences.getInt(gameModeKey, GameMode.Classic.ordinal)]
        set(value) = set(gameModeKey, value.ordinal)

    // Track the number of games completed
    var gamesCompleted: Int
        get() = preferences.getInt(gamesCompletedKey, 0)
        set(value) = set(gamesCompletedKey, value)

    // Get the highscore of the respective gameMode
    fun getHighScore(gameMode: GameMode): Int =
        preferences.getInt(gameMode.name + modeBestKey, 0)

    // Save a different highscore for each game mode
    fun saveHighScore(gameMode: GameMode, highscore: Int) =
        set(gameMode.name + modeBestKey, highscore)

    private fun set(name: String, value: Boolean) {
        editor.putBoolean(name, value)
        editor.apply()
    }

    private fun set(name: String, value: Int) {
        editor.putInt(name, value)
        editor.apply()
    }

    private fun set(name: String, value: String) {
        editor.putString(name, value)
        editor.apply()
    }
}
