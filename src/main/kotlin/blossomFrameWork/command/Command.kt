package blossomFrameWork.command

import blossomFrameWork.*
import blossomFrameWork.Console.Console
import blossomFrameWork.functions.*
import java.io.InputStream
import java.io.PrintStream
import java.util.*

class Command(val printStream: PrintStream = System.out, var inputStream: InputStream = System.`in`, build: InputFormatBuilder.() -> Unit) {
    private var prefix = ""
    private var thenString = " "
    private val runnableCommandMap = ArrayList<RunnableCommand>()//HashMap<String, HashMap<*,*>>()
    companion object{ const val isINPUT = "INPUT000" }//예약어 인풋


    init{
        val temp = InputFormatBuilder()
        build(temp)
        runnableCommandMap.addAll(temp.getCommands().withInfo(tag = "CommandBuild") { "User Added Command: ${it.transform { it.command }}" })
    }

    fun `in`(){
        //INPUT LOGIC
        if(BlossomSystem.isOnConsole){
            inputStream = Console.getInputStream()
        }
        val scan = Scanner(inputStream)
        val input = scan.nextLine().split(thenString)
        scan.close()



        val re = getCommandFromInput(input)
        if(re.second) re.first.forEach { runnableCommand ->
            try {
              runnableCommand.execution(getCommandContext(input,runnableCommand).withInfo { "Executing command: " +it.runnableCommand.command.joinToString(" ") })
            } catch (e: InputIsNotRequiredType) {

                if(re.first.size == 1){
                    error("${e.message}")
                }
                else log("[Commands] ${runnableCommand.declaration} is automatically disabled by User Input.")
            }
        }
        else{
            error("Cannot find any matches command")
            error("Available commands:")
            re.first.forEach {
                error("\t${it.command} : ${it.description}")
            }
        }



    }
    private fun getCommandFromInput(inputArray: List<String>): Pair<List<RunnableCommand>,Boolean>{
        //일치하는 명령어가 있는지 search
        val selectedCommands = ArrayList<RunnableCommand>()

        val a = HashMap<RunnableCommand,Int>()
        outer@for (runnableCommand in runnableCommandMap) {
            a[runnableCommand] = 0
            for ((i,e) in inputArray.withIndex()) {
                //info("CommandGetter") { "${runnableCommand.command[i]} != $e && ${!runnableCommand.command[i].contains(isINPUT)} == ${runnableCommand.command[i] != e && !runnableCommand.command[i].contains(isINPUT)}" }
                if(runnableCommand.command[i] != e && !runnableCommand.command[i].contains(isINPUT) || runnableCommand.command.size != inputArray.size){
                    continue@outer
                }
                a[runnableCommand] = a[runnableCommand]!!+1
            }
            selectedCommands.add(runnableCommand)
        }
        if(selectedCommands.isEmpty()){
            return a.getKeysFromValue(a.maxOf { it.value }) to false
        }

        return selectedCommands to true
    }

    private fun getCommandContext(inputArray: List<String>,runnableCommand: RunnableCommand): CommandContext{
        val args = ArrayList<String>()
        runnableCommand.command.filter { it.contains(isINPUT) }.forEach{
            val inputForArg = inputArray[runnableCommand.command.indexOf(it)] // 커스텀 ARG 에맞는 인풋을 가져오기
            val b = it.slice(it.indexOf(":")+isINPUT.length+1..it.lastIndex)

            try {
                inputForArg.toType(b)
                args += inputForArg
            } catch (_: NumberFormatException) {
                throw InputIsNotRequiredType("User's input is not Requested Type input, Requested \"${it.until(":")}\":$b")
            }
        }
        return CommandContext(runnableCommand,args,inputArray)
    }
    private fun getExecution(runnableCommand: RunnableCommand) =
        runnableCommand.execution
}
class InputIsNotRequiredType(msg : String) : Exception(msg)
data class RunnableCommand(val command: List<String>, val execution: (CommandContext) -> Unit, val description: String = ""){
    val declaration = command.first()
}