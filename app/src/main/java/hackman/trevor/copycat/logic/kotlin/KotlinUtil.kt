package hackman.trevor.copycat.logic.kotlin

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

// More linguistic version of writing `x == null`
@OptIn(ExperimentalContracts::class)
fun Any?.isNull(): Boolean {
    contract {
        returns(false) implies (this@isNull != null)
    }

    return this == null
}

// Property delegate for achieving a lateinit var with a setter, a feature not supported by the kotlin lateinit keyword
inline fun <T> lateinitSetter(
    crossinline before: (T) -> Unit = {},
    crossinline valueSet: (T) -> T = { it },
    crossinline after: (T) -> Unit = {}
) = object : ReadWriteProperty<Any?, T> {
    private var value: T? = null

    override operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value ?: throw IllegalStateException("Property ${property.name} should be initialized before get.")
    }

    override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        before(value)
        this.value = valueSet(value)
        after(value)
    }
}


//private class LateInitSetter<T>(val setter: (T) -> Unit) {
//    private var value: T? = null
//
//    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
//        return value ?: throw IllegalStateException("Property ${property.name} should be initialized before get.")
//    }
//
//    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
//        setter(value)
//        this.value = value
//    }
//}

