package code

import blossomFrameWork.frame.Frame
import blossomFrameWork.frame.Location
import blossomFrameWork.frame.Sprite
import blossomFrameWork.frame.drawSprite
import javax.swing.ImageIcon

class NewBackBoard {
    val frame = Frame(500,500)
    val sprite = Sprite()

    init {
        sprite.location = Location(250.0,250.0)
        sprite.img = ImageIcon("img").image


        frame.graphics.drawSprite(sprite)
        frame.repaint()
        frame.isVisible = true
    }
}