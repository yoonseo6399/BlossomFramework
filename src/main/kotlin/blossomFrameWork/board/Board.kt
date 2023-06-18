package blossomFrameWork.board

import blossomFrameWork.frame.Sprite
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import javax.lang.model.type.ArrayType
import javax.swing.JFrame
import javax.swing.tree.FixedHeightLayoutCache

class Board(width: Int,height: Int) : JFrame() {
    init {
        setSize(width,height)
        layout = null
        isVisible = true

    }

    override fun paint(g: Graphics) {
        super.paint(g)
        g.color = Color.CYAN
        (g as Graphics2D).stroke = BasicStroke(3f)
        g.drawRect(location.x+3,location.y+3,width-6,height-6)
        //spriteList.forEach { it.paint(g) }
    }
}