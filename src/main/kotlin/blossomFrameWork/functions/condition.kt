package blossomFrameWork.functions

fun <T> T.ifIsNull(action: Runnable): T{
    if (this == null) {
        action.run()
    }
    return this
}

fun <T : Any> T?.isNullException(exception : Throwable): T{
    if (this == null) {
        throw exception
    }
    return this as T
}

/*
if predicate is true then return or's value
 */
fun <T : Any?> T.insteadIf(or:T, predicate: (T) -> Boolean):T{
    return if(predicate(this)) or else this
}

infix fun <T : Any> T?.insteadIfNull(or:T):T{
    return this ?: or
}

infix fun <T : Any> T?.insteadIfNull(or:()->T):T{
    return this ?: or()
}

inline fun Boolean.ifTrue(action: () -> Unit){
    if(this){
        action()
    }
}

inline fun <T> Collection<T>.ifEmpty(action: () -> Unit) : Collection<T> {
    this.isEmpty().ifTrue(action)
    return this
}

inline fun <T> Collection<T>.ifNotEmpty(action: () -> Unit): Collection<T> {
    this.isNotEmpty().ifTrue(action)
    return this
}

fun <T> unless(predicate: Boolean,action: () -> T): T? = if(predicate) action() else null