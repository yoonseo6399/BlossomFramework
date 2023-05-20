package blossomFrameWork.Console

import blossomFrameWork.Frame.Frame
import blossomFrameWork.Game
import blossomFrameWork.application.Application
import blossomFrameWork.info
import blossomFrameWork.input.Command
import blossomFrameWork.input.request
import blossomFrameWork.isNullException
import blossomFrameWork.values.MutableValue
import code.CannotFindApplicationException
import kotlin.reflect.full.declaredMembers

object Console {
    fun showUI(loop:Boolean = false){
        Command{
            then("run"){
                then("game"){
                    then("gameName" request String::class){
                        executes { context ->

                            val game = Application.AllClasses.first { it.simpleName == context.argList[0] }::class.annotations.first { it.annotationClass == Game::class }

                        }
                    }
                }

            }
            then("Application"){
                then("run"){
                    then("name" request String::class){
                        then("functionName" request String::class){
                            executes {context ->
                                val app = Application.applicationMap.get(context.argList[0]).isNullException(
                                    CannotFindApplicationException("${context.argList[0]} is not defined in application")
                                ).first()
                                app::class.declaredMembers.first { it.name == context.argList[1] }.call(app)
                            }
                        }
                    }
                    then("name" request String::class){
                        executes { context ->
                            val app = Application.applicationMap.get(context.argList[0]).isNullException(
                                CannotFindApplicationException("${context.argList[0]} is not defined in application")
                            ).first()
                            app::class.declaredMembers.first { it.name == "run" }.call(app)
                        }
                    }
                }
            }
            then("Values"){
                then("MutableValueName" request String::class){
                    then("set"){
                        then("value" request String::class){
                            executes { context ->
                                var prefix = "Failed to "
                                if (MutableValue.list.first { it.name == context.argList[0] }.checkedSet(context.argList[1])) {
                                    prefix = "Successfully "
                                }
                                info(name = "Command") { prefix+"change the value : ${context.argList[0]} to ${context.argList[1]}" }
                            }
                        }
                    }
                }
            }
            then("Frame"){
                then("width" request String::class){
                    then("height" request String::class){
                        executes {
                            Frame(it.argList[0].toInt(),it.argList[1].toInt())
                        }
                    }
                }
            }
        }.`in`()
        if(loop) showUI(true)
    }
}