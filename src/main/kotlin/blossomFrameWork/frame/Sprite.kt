package blossomFrameWork.frame

import blossomFrameWork.frame.abstraction.Workable
import kotlinx.coroutines.*

class Sprite : Workable {
    val location = Location.getDefault
    val priority = 0
    var hitbox = HitBox.NONE
    //val img
    val name = "unknown"
    override val workMap = HashMap<String, Job>()
    override val workScope = CoroutineScope(Dispatchers.Main + Job() + CoroutineName("Sprite:$name"))


    fun smoothMove(location: Location,timeMill: Int){
        cancelTask("Moving")
        addTask("Moving") {
            val times = location/timeMill

            repeat (timeMill) {
                location += times
                delay(1)
            }
        }
    }

    infix fun isColludeWith(entity : Sprite) = entity.hitbox isColludeWith hitbox

    fun update(){
        TODO("Update Function")
    }
}