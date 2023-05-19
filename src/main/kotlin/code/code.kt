package code

import blossomFrameWork.BlossomSystem
import blossomFrameWork.runCode.Main

class Code {
    @Main
    fun main(){

        //println(Class.forName("java.lang."+Int::class.javaPrimitiveType?.name).kotlin)

        BlossomSystem.informationForDebug = true
        BlossomSystem.activeInfoTags["Calculator"] = true





    }

}