package galaxyraiders.core.game

import galaxyraiders.core.physics.Point2D
import galaxyraiders.core.physics.Vector2D

class Explosion(
  position: Point2D
) :
  SpaceObject("Explosion", '*', position, Vector2D(0.0, 0.0), 4.0, 0.0) {

  var is_triggered: Boolean = false
    private set

  fun triggered() {
    this.is_triggered = true
  }
}
