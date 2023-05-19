package code

import blossomFrameWork.*
import blossomFrameWork.application.Application
import blossomFrameWork.input.Command
import blossomFrameWork.input.request
import blossomFrameWork.values.MutableValue
import kotlin.reflect.full.declaredMembers

fun main() {
    Application.run(Code::class).main()
    Application.run(TEST::class)
    Application.run(Calculator::class)
    Application.setupApplication("code")

}

class CannotFindApplicationException(msg : String): Exception(msg)