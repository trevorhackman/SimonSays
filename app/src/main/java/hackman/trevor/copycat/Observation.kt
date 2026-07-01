package hackman.trevor.copycat

import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import hackman.trevor.tlibrary.observe.IObservable
import hackman.trevor.tlibrary.observe.Observable

/*
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

/**
 * Observes an [Observable] for the duration of the Fragment's view lifecycle.
 * Automatically adds the observer when the view is created and removes it when the view is destroyed.
 */
fun <T> Fragment.observe(
    observable: IObservable<T>,
    function: (T) -> Unit
) {
    viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onCreate(owner: LifecycleOwner) {
            observable.addObserver(function)
        }

        override fun onDestroy(owner: LifecycleOwner) {
            observable.removeObserver(function)
            owner.lifecycle.removeObserver(this)
        }
    })
}

/**
 * Observes an [Observable] for the duration of the LifecycleOwner's lifecycle.
 * Automatically adds the observer when the view is created and removes it when the view is destroyed.
 */
fun <T> LifecycleOwner.observe(
    observable: IObservable<T>,
    function: (T) -> Unit
) {
    lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onCreate(owner: LifecycleOwner) {
            println("Adding $function")
            observable.addObserver(function)
        }

        override fun onDestroy(owner: LifecycleOwner) {
            observable.removeObserver(function)
            owner.lifecycle.removeObserver(this)
        }
    })
}