package blossomFrameWork

import blossomFrameWork.Console.Console
import blossomFrameWork.functions.insteadIf
import blossomFrameWork.functions.unless
import java.io.InputStream
import javax.swing.text.Style

class BlossomSystem {
    companion object{
        var informationForDebug = false
        val activeInfoTags = hashMapOf("UnSet" to true)
        val activeContainTags = ArrayList<String>()
        var inputStream: InputStream = System.`in`
        var isOnConsole = false
    }
}

fun log(any: Any?, style: Style? = null){
    if(BlossomSystem.isOnConsole){
        Console.uploadLogln(any.toString(),style)
    }else println(any)
}
fun logWithoutln(any: Any?,style: Style? = null){
    if(BlossomSystem.isOnConsole){
        Console.uploadLog(any.toString(),style)
    }else println(any)
}
fun error(any: Any?){
    if(BlossomSystem.isOnConsole){
        Console.uploadLogln(any.toString(),Console.redStyle)
    }else println(any)
}

fun <T : Any?> T.withInfo(name: String = "UnSet", tag: String = "UnSet", transform: ((T) -> Any)? = null): T{
    //테그등록
    unless(BlossomSystem.activeInfoTags.keys.any{it == tag}) {
        BlossomSystem.activeInfoTags[tag] = false
    }

    if(BlossomSystem.informationForDebug && (BlossomSystem.activeInfoTags[tag]!! || BlossomSystem.activeContainTags.any{ tag.contains(it) })){
        log("[Info#${tag.insteadIf(name) { it == "UnSet" }}] ${insteadIf(transform!!(this)) { transform != null }}")
    }
    return this
}

fun info(name: String = "UnSet", tag: String = "UnSet", message: () -> Any) {
    if (!BlossomSystem.activeInfoTags.keys.any{it == tag}) {
        BlossomSystem.activeInfoTags[tag] = false
    }
    if(BlossomSystem.informationForDebug
        && (tag == "UnSet" || (BlossomSystem.activeInfoTags[tag]!! || BlossomSystem.activeContainTags.any{ tag.contains(it) }))){
        log("[Info#${tag.insteadIf(name) { it == "UnSet" }}] ${message()}")
    }
}