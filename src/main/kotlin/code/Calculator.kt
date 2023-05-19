package code

import blossomFrameWork.input.Command
import blossomFrameWork.input.request
import kotlin.math.pow
import kotlin.math.sqrt

class Calculator {
    fun run(){
        Command{
            then("firstNumber" request Int::class){
                then("secondNumber" request Int::class){
                    then("+"){
                        executes { println(it.argList[0].toInt()+it.argList[1].toInt()) }
                    }
                    then("-"){
                        executes { println(it.argList[0].toInt()-it.argList[1].toInt()) }
                    }
                    then("/"){
                        executes { println(it.argList[0].toInt()/it.argList[1].toInt()) }
                    }
                    then("*"){
                        executes { println(it.argList[0].toInt()*it.argList[1].toInt()) }
                    }
                    then("^"){
                        executes { println(Math.pow(it.argList[0].toDouble(),it.argList[1].toDouble())) }
                    }
                    then("root"){
                        executes { println(sqrt(it.argList[0].toDouble().pow(1.0/it.argList[1].toInt()))) }
                    }
                }
                then("root"){
                    executes { println(sqrt(it.argList[0].toDouble())) }
                }
            }
        }.`in`()
    }
}