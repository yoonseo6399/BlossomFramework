package blossomFrameWork.exceptions

import blossomFrameWork.insteadIf
import blossomFrameWork.reinforcedStackTrace.toTrace
import blossomFrameWork.toArrayList
import blossomFrameWork.transform
import blossomFrameWork.work
import java.util.*
import kotlin.Exception

open class Exception(val exception: Exception){
    val message = exception.cause!!.message

    var defaultSolution = "this is a Customized Exception. this may not have any solution"

    companion object{
        const val LOGO = "[ Blossom : Exception ] "
    }




    val trace = exception.cause!!.stackTrace.toTrace()//Throwable().fillInStackTrace().stackTrace.toTrace()

    fun findReason():List<String> {
        val reasonPrint = ArrayList<String>(50)
        val traces = trace.stackTrace.toArrayList().transform { "\t\t at $it" }
        //traces.removeAt(0);traces.removeAt(0)

        val a = exception.cause
        val exceptionName = exception.cause!!::class.java.toString().work { slice(
            lastIndexOf('.')+1 .. (lastIndexOf(':')-1).insteadIf(lastIndex) { it < 0 }
        ) }



        val exceptionUseLib = exception.cause!!.toString().work { slice(
            lastIndexOf('.',lastIndexOf('.',indexOf(exceptionName))-1)+1 until lastIndexOf('.',indexOf(exceptionName))
        ) }

        with(reasonPrint) {
            add(LOGO)
            add("\tWhile using ${exceptionUseLib.uppercase(Locale.getDefault())} $exceptionName occurred : $message")
            add("\tdefault Solution: $defaultSolution")
            addAll(traces)
        }

        return reasonPrint
    }
}


