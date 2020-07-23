package hackman.trevor.copycat

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * [LiveData] is not null safe
 * These extension functions allow null-safe observation
 *
 * Fragments have two life cycles
 * These extension functions force the use of the fragment's second life cycle, viewLifecycleOwner
 */

inline fun <T : Any?> Fragment.observe(
    liveData: LiveData<T>,
    crossinline function: (T) -> Unit
) =
    liveData.observe(viewLifecycleOwner, Observer {
        function(it)
    })

@JvmName("observeNotNull")
inline fun <T : Any> Fragment.observe(
    liveData: LiveData<T>,
    crossinline function: (T) -> Unit
) =
    liveData.observe(viewLifecycleOwner, Observer {
        function(it ?: return@Observer)
    })

inline fun <T : Any?> LifecycleOwner.observe(
    liveData: LiveData<T>,
    crossinline function: (T) -> Unit
) =
    liveData.observe(this, Observer {
        function(it)
    })

@JvmName("observeNotNull")
inline fun <T : Any> LifecycleOwner.observe(
    liveData: LiveData<T>,
    crossinline function: (T) -> Unit
) =
    liveData.observe(this, Observer {
        function(it ?: return@Observer)
    })

fun <T> LiveData<T>.requireValue() = value!!
