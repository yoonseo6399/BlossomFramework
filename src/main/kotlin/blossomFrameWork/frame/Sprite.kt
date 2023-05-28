package blossomFrameWork.frame

import blossomFrameWork.frame.abstraction.Workable
import kotlinx.coroutines.*
import java.awt.Graphics
import java.awt.Image
import java.lang.RuntimeException

class Sprite : Workable {
    var location = Location.getDefault
    val priority = 0
    var hitbox = HitBox.NONE
    var img: Image? = null
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

fun Graphics.drawSprite(sprite: Sprite){
    if(sprite.img == null) throw RuntimeException("Sprite's img is null")
    drawImage(sprite.img,sprite.location.x.toInt(),sprite.location.y.toInt(),null)
}