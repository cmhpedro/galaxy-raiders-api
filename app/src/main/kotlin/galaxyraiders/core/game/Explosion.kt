package galaxyraiders.core.game

import java.util.Timer
import java.util.TimerTask
import galaxyraiders.core.physics.Point2D
import galaxyraiders.core.physics.Vector2D

class Explosion(
  position: Point2D,
  radius: Double
) :
  SpaceObject("Explosion", '*', position, Vector2D(0.0, 0.0), radius, 0.0) {

  var isTriggered: Boolean = true
    private set

  fun triggered() {
    this.isTriggered = false
  }

  init {
    var timerTask = object : TimerTask() {
      override fun run() {
        triggered()
      }
    }

    Timer().schedule(timerTask, 1000)
  }
}
