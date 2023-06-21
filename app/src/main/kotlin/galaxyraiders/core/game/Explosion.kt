package galaxyraiders.core.game

import galaxyraiders.core.physics.Point2D
import galaxyraiders.core.physics.Vector2D
import java.util.Timer
import java.util.TimerTask

const val MILLISECONDS_ON_SCREEN: Int = 1000

class Explosion(
  position: Point2D,
  radius: Double
) :
  SpaceObject("Explosion", '*', position, Vector2D(0.0, 0.0), radius, 0.0) {

  var isTriggered: Boolean = true
    private set

  init {
    var timerTask = object : TimerTask() {
      override fun run() {
        isTriggered = false
      }
    }
    Timer().schedule(timerTask, MILLISECONDS_ON_SCREEN.toLong())
  }
}
