package blossomFrameWork.frame

import java.awt.Graphics
@Suppress("UNUSED")
data class HitBox(val loc1 : Location, val loc2: Location) {
    companion object{
        val NONE: HitBox = HitBox(Location.getDefault,Location.getDefault)
    }

    val x = minOf(loc1.x,loc2.x)
    val y = minOf(loc1.y,loc2.y)

    val width = maxOf(loc1.x,loc2.x)
    val height = maxOf(loc1.y,loc2.y)

    val boxLayoutX = x.. x+width
    val boxLayoutY = y .. y+width

    fun isZero(): Boolean =
        loc1.isZero() && loc2.isZero() || loc1 == loc2

    fun draw(g: Graphics){
        g.drawRect(x.toInt(), y.toInt(), width.toInt(), height.toInt())
    }

    infix fun isColludeWith(box1: HitBox): Boolean {
        return box1.x in boxLayoutX && box1.y in boxLayoutY
    }
    fun moveTo(location: Location){
        val movingVec = location - Location(x,y)
        loc1 += movingVec
        loc2 += movingVec
    }
}