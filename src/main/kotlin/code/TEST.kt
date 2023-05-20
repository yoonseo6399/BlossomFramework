package code

import blossomFrameWork.log
import blossomFrameWork.values.MutableValue

class TEST {

    val value by MutableValue(1,"value")
    fun wow(){
        log( value)
    }
}