package blossomFrameWork

import blossomFrameWork.Console.Console
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