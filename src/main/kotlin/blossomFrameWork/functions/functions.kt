package blossomFrameWork.functions

import blossomFrameWork.log
import java.lang.Exception

fun <T : Any,R: Any> List<T>.transform(transform: (T) -> R): ArrayList<R> {
    val list = ArrayList<R>()
    for ((i,e) in this.withIndex()) {
        list.add(transform(e))
    }
    return list
}

fun <T> Array<T>.toArrayList(): ArrayList<T> {
    val list = ArrayList<T>()
    this.forEach {
        list.add(it)
    }
    return list
}

fun <T> T.print(transform: ((T) -> String)? = null){
    if(transform == null) {
        log(this)
    }else log(transform(this))
}
fun <T> T.alsoPrint(transform: ((T) -> String)? = null): T{
    if(transform == null) {
        log(this)
    }else log(transform(this))
    return this
}

fun <T> List<T>.getOrNull(index: Int):T?=
    try {
        get(index)
    }catch (e: Exception){ null }

fun <T,R : Any> T.work(action: T.() -> R) = action(this)


fun String.between(start:String,end:String):String{
    return this.substring(this.indexOf(start) + start.length, this.indexOf(end))
}
fun String.until(end:String):String{
    return this.substring(0, this.indexOf(end))
}
fun <K, V> HashMap<K, V>.getKeysFromValue(value: V): List<K> {
    val dest = ArrayList<K>()
    for ((key, v) in this) {
        if (v == value) {
            dest.add(key)
        }
    }
    return dest
}


operator fun <T> ArrayList<T>.plusAssign(element: T) { this.add(element) }