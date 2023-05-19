package blossomFrameWork

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

fun <T> T.isNullOr(action: Runnable): T{
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

fun <T> T.print(transform: ((T) -> String)? = null){
    if(transform == null) {
        println(this)
    }else println(transform(this))
}
fun <T> T.alsoPrint(transform: ((T) -> String)? = null): T{
    if(transform == null) {
        println(this)
    }else println(transform(this))
    return this
}

fun <T : Any?> T.withInfo(tag:String = "UnSet",transform: ((T) -> String)? = null): T{
    //테그등록
    if (!BlossomSystem.activeInfoTags.keys.any{it == tag}) {
        BlossomSystem.activeInfoTags[tag] = false
    }
    if(BlossomSystem.informationForDebug && (BlossomSystem.activeInfoTags[tag]!! || BlossomSystem.activeContainTags.any{ tag.contains(it) })){
        println("[Info for #$tag] ${this.insteadIf(transform!!(this)) {transform != null}}")
    }
    return this
}

fun info(tag:String = "UnSet",message: (() -> String)) {
    if (!BlossomSystem.activeInfoTags.keys.any{it == tag}) {
        BlossomSystem.activeInfoTags[tag] = false
    }
    if(BlossomSystem.informationForDebug && (BlossomSystem.activeInfoTags[tag]!! || BlossomSystem.activeContainTags.any{ tag.contains(it) })){
        println("[Info for #$tag] ${message()}")
    }
}

fun <T,R : Any> T.work(action: T.() -> R) = action(this)


/*
if predicate is true then return or's value
 */
fun <T : Any?> T.insteadIf(or:T, predicate: (T) -> Boolean):T{
    return if(predicate(this)) or else this
}
fun <T : Any?> T.insteadIfNull(or:T):T{
    return this ?: or
}

fun String.between(start:String,end:String):String{
    return this.substring(this.indexOf(start) + start.length, this.indexOf(end))
}
fun String.until(end:String):String{
    return this.substring(0, this.indexOf(end))
}

infix fun <T> ArrayList<T>.add(element: T) = add(element)
fun <K, V> HashMap<K, V>.getKeysFromValue(value: V): List<K> {
    val dest = ArrayList<K>()
    for ((key, v) in this) {
        if (v == value) {
            dest.add(key)
        }
    }
    return dest
}


operator fun <T> ArrayList<T>.plusAssign(element: T) { this.add(element)}