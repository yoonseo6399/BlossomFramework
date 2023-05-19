package blossomFrameWork.input

import blossomFrameWork.info
import blossomFrameWork.isNullException
import blossomFrameWork.withInfo
import kotlin.reflect.KClass

typealias Execution = ((CommandContext) -> Unit)

class InputFormatBuilder {
    private val contexts = ArrayList<Pair<String,InputFormatBuilder?>>()
    private var execution : ((CommandContext) -> Unit)? = null
    companion object{
        private const val INFERIOR = "INFERIOR_NODE000"
    }

//    private val executable =

    fun getCommands(): List<RunnableCommand>{
        val commands = ArrayList<RunnableCommand>()

        for (commandLine in commandStringWithExecution()) {
            commands += RunnableCommand(commandLine.first.split('.'),commandLine.second.
            isNullException(CannotFoundExecutionException("Command: ${commandLine.first} has no Execution")))
        }
        return commands
    }



    private fun commandStringWithExecution(): List<Pair<String,Execution?>>{ // <- CommandLine
        val result = ArrayList<Pair<String,Execution?>>()

        if(contexts.isEmpty()) return listOf(INFERIOR to execution).withInfo("CommandBuildContext") { "Inferior node $this returning execution" }

        for(context in contexts){
            context.second?.let { child ->
                child.commandStringWithExecution().forEach { childCommands -> // transform 으로 바꿀수있을듯. 아닌가
                    if(childCommands.first == INFERIOR){ //child 가 Inferior node -> Execution 반환

                        info("CommandBuildContext") { "Execution returning detected , adding ${childCommands.second}" }
                        result.add(context.first to childCommands.second)// execution 등록하기
                    }else result.add(context.first + "." + childCommands.first to childCommands.second).withInfo("CommandBuildContext") { "Inferior Node is not My Child, running default operation" }
                }
            }
        }
        return result
    }
    fun then(context : String,builder: InputFormatBuilder.() -> Unit){
        val build = InputFormatBuilder()
        builder(build)
        contexts.add(context to build)
        info("CommandBuilderThen") { "then built on $context" }
    }
    fun executes(action: (CommandContext) -> Unit){
        this.execution = action
        info("CommandBuilderExecutes") { "executes action : $action" }
    }
}

infix fun String.request(requestType: KClass<*>) = "$this:${Command.isINPUT}${requestType.java.typeName}"


class ArgumentTypes{ // 메테리얼처럼 상수로 바꿀가 생각중 String 말고 알규타입 강제하게
    companion object{
        val INT     = Int::class
        val STRING  = String::class
        val DOUBLE  = Double::class
        val BOOLEAN = Boolean::class
        val FLOAT   = Float::class
        val LONG    = Long::class
    }
}

//enum class ArgumentTypes{
//
//}