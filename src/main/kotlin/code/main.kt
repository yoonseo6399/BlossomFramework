package code

import blossomFrameWork.*
import blossomFrameWork.Console.Console
import blossomFrameWork.application.Application
import kotlinx.coroutines.*
import java.io.ByteArrayInputStream
import java.util.Scanner


fun main() {
    Application.run(Code::class).main()
    Application.run(TEST::class)
    Application.run(Calculator::class)
    Application.setupApplication("code")

    val input = ByteArrayInputStream("this is input".toByteArray())
    println(Scanner(input).nextLine())







    Console.showFrame()
    log("Press enter to exit")
    println("runned?")
    Console.showUI(true)
    val ConsoleUpdater = CoroutineScope(Dispatchers.Main + Job() + CoroutineName("ConsoleUpdate"))
    //Console.showUI(true)
}

class CannotFindApplicationException(msg : String): Exception(msg)