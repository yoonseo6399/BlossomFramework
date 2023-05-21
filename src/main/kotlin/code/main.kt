package code

import blossomFrameWork.*
import blossomFrameWork.Console.Console
import blossomFrameWork.application.Application
import kotlinx.coroutines.*
import java.io.ByteArrayInputStream
import java.util.Scanner


fun main() {

    Application.add(Code::class).main()
    Application.add(Calculator::class)
    Application.add(TEST::class)
    Application.add(OneCard_Game::class)
    Application.setupApplication("code")




    Console.showFrame()
    Console.showUI(true)









}

class CannotFindApplicationException(msg : String): Exception(msg)