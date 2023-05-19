package code

import blossomFrameWork.BlossomSystem
import blossomFrameWork.runCode.Main

class Code {
    @Main
    fun main(){
        BlossomSystem.informationForDebug = true
        BlossomSystem.activeInfoTags["Calculator"] = true
    }
}