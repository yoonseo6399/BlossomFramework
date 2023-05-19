

package blossomFrameWork.runCode

import blossomFrameWork.isNullException
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.reflect.KClass
import kotlin.reflect.full.memberFunctions

object CodeLoader{
    @OptIn(DelicateCoroutinesApi::class)
    val scope = GlobalScope


    fun <T : Any> load(clazz: KClass<T>) : T{
        try {
            return clazz.constructors.first { it.parameters.isEmpty() }.call()
        } catch (_: Exception) {
            throw CannotInvokeException("cannot invoke constructor for $clazz, please check if the constructor has no parameters")
        }
    }
    @OptIn(DelicateCoroutinesApi::class)
    fun <T : Any> loadAndRun(clazz: KClass<T>, withCoroutine: Boolean = false){

        val loadedClass = load(clazz)
        val mainMethod =
            loadedClass::class.memberFunctions.
            find { function -> function.annotations.any { it.annotationClass == Main::class } && function.parameters.size == 1 }.//find annotated function main
            isNullException(CannotInvokeException("Cannot find method annotated with '@Main' , please check method's parameters and declaration"))



        try {
            if (withCoroutine) scope.launch { mainMethod.call(loadedClass) } else mainMethod.call(loadedClass)
        } catch(e: Exception) {
            blossomFrameWork.exceptions.Exception(e).findReason().forEach(System.err::println)
        }
    }
}

class CannotInvokeException(msg: String): Exception(msg)
