package blossomFrameWork.frame

data class Location(var x: Double, var y: Double) {
    companion object{
        val getDefault: Location
            get() = Location(0.0, 0.0)
    }

    fun isZero(): Boolean = x == 0.0 && y == 0.0

    operator fun plus(location: Location): Location {
        return Location(this.x+location.x, this.y+location.y)
    }
    operator fun plusAssign(location: Location) {
        this.x += location.x
        this.y += location.y
    }
    operator fun minus(location: Location): Location {
        return Location(this.x-location.x, this.y-location.y)
    }
    operator fun minusAssign(location: Location) {
        this.x -= location.x
        this.y -= location.y
    }
    operator fun times(location: Location): Location {
        return Location(this.x*location.x, this.y*location.y)
    }
    operator fun timesAssign(location: Location) {
        this.x *= location.x
        this.y *= location.y
    }
    operator fun div(location: Location): Location {
        return Location(this.x/location.x, this.y/location.y)
    }
    operator fun divAssign(location: Location) {
        this.x /= location.x
        this.y /= location.y
    }

    operator fun div(num: Int): Location {
        return Location(this.x/num, this.y/num)
    }
    operator fun divAssign(num: Int) {
        this.x /= num
        this.y /= num
    }

}