package code

import blossomFrameWork.application.Inject
import blossomFrameWork.log
import blossomFrameWork.values.MutableValue

class TEST(@Inject val cal : Calculator) {

    val value by MutableValue(1,"value")
    fun wow(){
        log(value)
    }
    fun cal(){
        cal.run()
    }
}