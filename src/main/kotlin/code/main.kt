package code

import blossomFrameWork.*
import blossomFrameWork.Console.Console
import blossomFrameWork.application.Application


fun main() {
    Application.run(Code::class).main()
    Application.run(TEST::class)
    Application.run(Calculator::class)
    Application.setupApplication("code")

    Console.showUI(true)
}

class CannotFindApplicationException(msg : String): Exception(msg)