package hackman.trevor.copycat

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

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
        it ?: return@Observer
        function(it)
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
        it ?: return@Observer
        function(it)
    })