// @file:Suppress("UNUSED_PARAMETER") // <- REMOVE
package galaxyraiders.core.physics

data class Point2D(val x: Double, val y: Double) {
  operator fun plus(p: Point2D): Point2D {
    val NewX: Double = x + p.x
    val NewY: Double = y + p.y
    return Point2D(NewX, NewY)
  }

  operator fun plus(v: Vector2D): Point2D {
    val NewX: Double = x + v.dx
    val NewY: Double = y + v.dy
    return Point2D(NewX, NewY)
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
    val DeltaX: Double = Math.abs(x - p.x)
    val DeltaY: Double = Math.abs(y - p.y)
    return Math.sqrt(DeltaX * DeltaX + DeltaY * DeltaY)
  }
}
