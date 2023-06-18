package blossomFrameWork.frame

class percentageLocation(val xp: Double, val yp : Double) : Location(screenSize.x/xp, screenSize.y/yp) {
    companion object{
        lateinit var screenSize : Location
    }
    fun update(){
        x = screenSize.x/xp
        y= screenSize.y/yp
    }
}