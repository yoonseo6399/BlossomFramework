package blossomFrameWork.frame

import blossomFrameWork.log
import java.awt.Component
import java.awt.FlowLayout
import java.awt.Graphics
import java.awt.event.*
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JTextField

class Frame(private val width: Int, private val height: Int) : JFrame() {
    val sprites = ArrayList<Sprite>()
    init {
        percentageLocation.screenSize = Location(height,width)
        setSize(width,height)
        layout= FlowLayout()
        isVisible = true
    }

    override fun paint(g: Graphics) {
        g.clearRect(0, 0, width, height)
        sprites.forEach { it.paint(g) }
    }

    override fun paintComponents(g: Graphics) {
        sprites.forEach { it.paint(g) }
    }
}

fun Component.whenKeyPressed( block: (KeyEvent) -> Unit){
    this.addKeyListener(object : KeyAdapter() {
        override fun keyPressed(e: KeyEvent) {
             block(e)
        }
    })
}