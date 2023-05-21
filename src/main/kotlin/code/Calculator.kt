package code

import blossomFrameWork.command.Command
import blossomFrameWork.command.request
import blossomFrameWork.info
import kotlin.math.pow
import kotlin.math.sqrt

class Calculator {
    fun run(){
        Command{
            then("firstNumber" request Int::class){
                then("secondNumber" request Int::class){
                    then("+"){
                        executes { info("Calculator"){it.argList[0].toInt()+it.argList[1].toInt()} }
                    }
                    then("-"){
                        executes { info("Calculator"){it.argList[0].toInt()-it.argList[1].toInt()} }
                    }
                    then("/"){
                        executes { info("Calculator"){it.argList[0].toInt()/it.argList[1].toInt()} }
                    }
                    then("*"){
                        executes { info("Calculator"){it.argList[0].toInt()*it.argList[1].toInt()} }
                    }
                    then("^"){
                        executes { info("Calculator"){Math.pow(it.argList[0].toDouble(),it.argList[1].toDouble())} }
                    }
                    then("root"){
                        executes { info("Calculator"){sqrt(it.argList[0].toDouble().pow(1.0/it.argList[1].toInt()))} }
                    }
                }
                then("root"){
                    executes { info("Calculator"){sqrt(it.argList[0].toDouble()) } }
                }
            }
        }.`in`()
    }
}