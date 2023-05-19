package blossomFrameWork.reinforcedStackTrace

import blossomFrameWork.toArrayList
import blossomFrameWork.transform

class Trace(val stackTrace: Array<StackTraceElement>) {




    fun print(){
        stackTrace.toArrayList().transform { it.toString() }.forEach{ System.err.println("\t at $it") }
    }
}
fun Array<StackTraceElement>.toTrace():Trace =
    Trace(this)
infix fun <T : Any> Class<T>.hasSuperClass(clazz: Class<*>): Boolean {
    return this.isAssignableFrom(clazz)
}