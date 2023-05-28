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
    val graphic : Graphics
    init {
        setSize(width,height)
        layout= FlowLayout()
        this.graphic = graphics
    }

    override fun paint(g: Graphics) {
        super.paint(g)
    }
}

object Listener: ActionListener{
    override fun actionPerformed(e: ActionEvent?) {
        log("Action")
    }
}
fun JButton.createActionBlock(block: (ActionEvent) -> Unit){
    this.addActionListener(block)
}
fun JTextField.whenKeyTyped(block: (KeyEvent) -> Unit){
    this.addKeyListener(object : KeyAdapter() {
        override fun keyPressed(e: KeyEvent) {
            block(e)
        }
    })
}
fun Component.whenKeyTyped(keyId: Int, block: (KeyEvent) -> Unit){
    this.addKeyListener(object : KeyAdapter() {
        override fun keyTyped(e: KeyEvent) {
            if (e.keyCode == keyId) block(e)
        }
    })
}
fun Component.whenKeyPressed(keyId: Int, block: (KeyEvent) -> Unit){
    this.addKeyListener(object : KeyAdapter() {
        override fun keyPressed(e: KeyEvent) {
            if (e.keyCode == keyId) block(e)
        }
    })
}
fun Component.whenKeyPressed( block: (KeyEvent) -> Unit){
    this.addKeyListener(object : KeyAdapter() {
        override fun keyPressed(e: KeyEvent) {
             block(e)
        }
    })
}