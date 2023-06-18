package blossomFrameWork.frame

import blossomFrameWork.frame.abstraction.Workable
import blossomFrameWork.functions.alsoPrint
import blossomFrameWork.info
import kotlinx.coroutines.*
import java.awt.Graphics
import java.awt.Image
import java.awt.image.BufferedImage
import java.lang.RuntimeException
import javax.imageio.plugins.jpeg.JPEGHuffmanTable
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.SwingUtilities

class Sprite(val frame: JFrame) : Workable, JPanel() {
    var location = Location.getDefault
    val priority = 0
    var hitbox = HitBox.NONE
    var img: Image? = null

    override val workMap = HashMap<String, Job>()
    override val workScope = CoroutineScope(Dispatchers.Default + Job() + CoroutineName("Sprite:$name"))
    private var bufferImage: BufferedImage? = null

    fun smoothMove(location: Location,timeMill: Int){
        cancelTask("Moving")
        addTask("Moving") {
            val times = location/timeMill

            repeat (timeMill+1) {

                this@Sprite.location.plusAssign(times)
                info { this@Sprite.location }
                delay(1)
                SwingUtilities.invokeLater {
                    frame.repaint()
                }
            }
        }
    }

    override fun paint(g : Graphics){
        if(img == null) throw RuntimeException("Sprite's img is null")

        if (bufferImage == null || bufferImage?.width != frame.width || bufferImage?.height != frame.height) {
            bufferImage = BufferedImage(frame.width, frame.height, BufferedImage.TYPE_INT_ARGB)
        }

        val bufferGraphics = bufferImage?.createGraphics()

        // 이미지 그리기 코드
        bufferGraphics?.drawImage(img,location.x.toInt().alsoPrint(), location.y.toInt(),null)

        // 임시 버퍼를 화면에 그립니다.
        g.drawImage(bufferImage, 0, 0, null)
    }

    infix fun isColludeWith(entity : Sprite) = entity.hitbox isColludeWith hitbox
}

