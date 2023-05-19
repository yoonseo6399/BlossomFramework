package blossomFrameWork.input

import blossomFrameWork.*
import code.CannotFindApplicationException
import java.io.PrintStream
import java.util.*

class Command(printStream: PrintStream = System.out, build: InputFormatBuilder.() -> Unit) {
    private var prefix = ""
    private var thenString = " "
    private val runnableCommandMap = ArrayList<RunnableCommand>()//HashMap<String, HashMap<*,*>>()
    companion object{ const val isINPUT = "INPUT000" }//예약어 인풋


    init{
        val temp = InputFormatBuilder()
        build(temp)
        runnableCommandMap.addAll(temp.getCommands().withInfo("CommandBuild") { "User Added Command: ${it.transform { it.command }}" })
    }

    fun `in`(){
        //INPUT LOGIC
        val scan = Scanner(System.`in`)
        val input = scan.nextLine().split(thenString) //IO BLOCK // 사용자가 지정한걸로 쪼갠다

        val re = getCommandFromInput(input)
        if(re.second) re.first.forEach { runnableCommand ->
            try {
              runnableCommand.execution(getCommandContext(input,runnableCommand).withInfo { "Executing command: " +it.runnableCommand.command.joinToString(" ") })
            } catch (e: IllegalArgumentException) {
                if(re.first.size == 1){
                    System.err.println("${e.message}")
                }
                else println("[Commands] ${runnableCommand.declaration} is automatically disabled by User Input.")
            }
        }
        else{
            System.err.println("Cannot find any matches command")
            System.err.println("Available commands:")
            re.first.forEach {
                System.err.println("\t${it.command} : ${it.description}")
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
                throw IllegalArgumentException("User's input is not Requested Type input, Requested \"${it.until(":")}\":$b")
            }
        }
        return CommandContext(runnableCommand,args,inputArray)
    }
    private fun getExecution(runnableCommand: RunnableCommand) =
        runnableCommand.execution
}

data class RunnableCommand(val command: List<String>, val execution: (CommandContext) -> Unit, val description: String = ""){
    val declaration = command.first()
}