package code

import blossomFrameWork.values.MutableValue

class TEST {

    val value by MutableValue(1,"value")
    fun wow(){
        println(value)
    }
}