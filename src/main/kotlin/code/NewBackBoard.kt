package code

import blossomFrameWork.board.Board
import blossomFrameWork.board.Entity
import blossomFrameWork.frame.Frame
import blossomFrameWork.frame.Location
import blossomFrameWork.frame.Sprite
import blossomFrameWork.functions.alsoPrint
import blossomFrameWork.functions.inRandom
import blossomFrameWork.info
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.awt.Point
import javax.swing.ImageIcon
import javax.swing.SwingUtilities

class NewBackBoard {
    val frame = Frame(500,500)
    val sprite = Sprite(frame)
    init {
        sprite.img = ImageIcon("A:\\BlossomFramework\\src\\main\\kotlin\\code\\img.jpg").image

        frame.add(sprite)
        frame.repaint()
        frame.isVisible = true
        move()
        runBlocking {
            delay(1000*2)
        }
    }


    fun move(){
        info { "moved" }
        sprite.smoothMove(Location(100,100),1000*2)
        frame.isVisible = true
    }
}
class NewBackBoard2 {
    var entity : Entity? = null
    val frame = Board(1000,1000).apply {
        //val e = Entity("A:\\BlossomFramework\\src\\main\\kotlin\\code\\img.jpg").also(::add).also { entity = it }
    }

    init {
        GlobalScope.launch {
            repeat(100){
                //entity!!.smoothMove((0 inRandom 1000).alsoPrint(),0 inRandom 1000,100)
                entity = Entity("A:\\BlossomFramework\\src\\main\\kotlin\\code\\img.jpg").also(frame::add).also { entity = it }.apply {
                    location = Point(100,100)
                }
                entity!!.applyVelocity(0.0,-50.0)

                delay(1000)
            }
        }
    }
}