package blossomFrameWork.Frame

import javax.swing.JFrame

class Frame(private val width: Int, private val height: Int) : JFrame() {
    init {
        setSize(width,height)
        isVisible = true
    }
}