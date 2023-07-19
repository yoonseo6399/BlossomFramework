package code

import blossomFrameWork.board.Board
import blossomFrameWork.board.Entity
import blossomFrameWork.frame.Frame
import blossomFrameWork.frame.Location
import blossomFrameWork.frame.Sprite
import blossomFrameWork.info
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.awt.Image
import java.awt.Point
import javax.swing.ImageIcon

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
    val board = Board(1000,1000)

    init {
        //GlobalScope.launch {
            repeat(100){
                val img = ImageIcon(ImageIcon(
                    "A:\\BlossomFramework\\src\\main\\kotlin\\code\\onecard\\cards\\4S.png"
                ).image.getScaledInstance(90,150, Image.SCALE_SMOOTH))
                Entity(img).let {
                    board.add(it)
                    it.location = Point(100,100)
                    runBlocking {
                        it.smoothMove(0,-50,100)
                    }
                }
            }
        //}
    }
}