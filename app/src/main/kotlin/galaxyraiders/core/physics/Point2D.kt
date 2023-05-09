// @file:Suppress("UNUSED_PARAMETER") // <- REMOVE
package galaxyraiders.core.physics

data class Point2D(val x: Double, val y: Double) {
  operator fun plus(p: Point2D): Point2D {
    val newX: Double = x + p.x
    val newY: Double = y + p.y
    return Point2D(newX, newY)
  }

  operator fun plus(v: Vector2D): Point2D {
    val newX: Double = x + v.dx
    val newY: Double = y + v.dy
    return Point2D(newX, newY)
  }

  override fun toString(): String {
    return "Point2D(x=$x, y=$y)"
  }

  fun toVector(): Vector2D {
    return Vector2D(x, y)
  }

  fun impactVector(p: Point2D): Vector2D {
    val dx: Double = Math.abs(x - p.x)
    val dy: Double = Math.abs(y - p.y)
    return Vector2D(dx, dy)
  }

  fun impactDirection(p: Point2D): Vector2D {
    val dx: Double = Math.abs(x - p.x)
    val dy: Double = Math.abs(y - p.y)
    return Vector2D(dx, dy)
  }

  fun contactVector(p: Point2D): Vector2D {
    val impactVersor: Vector2D = impactVector(p)
    return impactVersor.normal
  }

  fun contactDirection(p: Point2D): Vector2D {
    val impactVersor: Vector2D = impactVector(p)
    return impactVersor.normal
  }

  fun distance(p: Point2D): Double {
    val deltaX: Double = Math.abs(x - p.x)
    val deltaY: Double = Math.abs(y - p.y)
    return Math.sqrt(deltaX * deltaX + deltaY * deltaY)
  }
}
