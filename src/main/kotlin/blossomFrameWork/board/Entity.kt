package blossomFrameWork.board

import blossomFrameWork.functions.alsoPrint
import blossomFrameWork.info
import kotlinx.coroutines.*
import java.awt.*
import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.SwingUtilities
import kotlin.math.absoluteValue
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis
import kotlin.time.measureTime

class Entity(imagePath: String) : JLabel() {
    val image = ImageIcon(imagePath).image
    private var velocity_x = 0.0
    private var velocity_y = 0.0
    private var velocityCorutine : Job? = null
    private val movingScope = CoroutineScope(Dispatchers.Default + CoroutineName("Moving Task") + Job())
    init {
        size = Dimension(image.getWidth(null), image.getHeight(null))
        preferredSize = Dimension(image.getWidth(null), image.getHeight(null))
        layout = null
        icon = ImageIcon(imagePath)
    }

    fun applyVelocity(x:Double,y:Double){
        velocity_x = x
        velocity_y = y
        if(velocityCorutine?.isActive == true) return

        velocityCorutine = movingScope.launch {
            while (velocity_x.absoluteValue + velocity_y.absoluteValue > 0.5){
                runBlocking {
                    smoothMove(location.x + velocity_x.toInt(),location.y+velocity_y.toInt(),10)
                }
                //delay(100)

                info { location }
                SwingUtilities.invokeLater {
                    repaint()
                }
                //감소
                velocity_x/=1.2
                velocity_y/=1.2

            }
        }
    }




    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        //g.drawImage(image,location.x, location.y,null)
        //g.color = Color.CYAN
        //(g as Graphics2D).stroke = BasicStroke(3f)
        //g.drawRect(location.x,location.y,width,height)

    }

    suspend fun smoothMove(x:Int, y:Int, timeMills: Int){
        movingScope.launch {
            val dtx = ((x.toDouble()-location.x)/timeMills)
            val dty = ((y.toDouble()-location.y)/timeMills)
            var initialLocation_x = location.x.toDouble()
            var initialLocation_y = location.y.toDouble()
            var startingTime = System.currentTimeMillis()
            repeat(timeMills){
                repeat(2){
                    initialLocation_x += (dtx/2)
                    initialLocation_y += (dty/2)
                    val newLocation = Point(initialLocation_x.toInt(),initialLocation_y.toInt())
                    location = newLocation
                    SwingUtilities.invokeLater {
                        repaint()
                    }
                }
                delay(1)
            }
        }.join()

    }
}