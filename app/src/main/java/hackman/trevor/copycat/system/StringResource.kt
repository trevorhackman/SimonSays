package hackman.trevor.copycat.system

import androidx.annotation.StringRes

/**
 * @param string is expected to be a string resource reference (e.g. android.R.string.ok)
 */
data class StringResource(@StringRes val string: Int)
