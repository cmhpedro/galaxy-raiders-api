// @file:Suppress("UNUSED_PARAMETER") // <- REMOVE
package galaxyraiders.core.physics

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties("unit", "normal", "degree", "magnitude")
data class Vector2D(val dx: Double, val dy: Double) {
  override fun toString(): String {
    return "Vector2D(dx=$dx, dy=$dy)"
  }

  val magnitude: Double
    get() = Math.sqrt(dx * dx + dy * dy)

  val radiant: Double
    get() = Math.atan2(dy, dx)

  val degree: Double
    get() = Math.toDegrees(radiant)

  val unit: Vector2D
    get() = div(magnitude)

  val normal: Vector2D
    get() = Vector2D(dy / magnitude, -dx / magnitude)

  operator fun times(scalar: Double): Vector2D {
    val newDx: Double = dx * scalar
    val newDy: Double = dy * scalar
    return Vector2D(newDx, newDy)
  }

  operator fun div(scalar: Double): Vector2D {
    val newDx: Double = dx / scalar
    val newDy: Double = dy / scalar
    return Vector2D(newDx, newDy)
  }

  operator fun times(v: Vector2D): Double {
    val x: Double = dx * v.dx
    val y: Double = dy * v.dy
    return x + y
  }

  operator fun plus(v: Vector2D): Vector2D {
    val newDx: Double = dx + v.dx
    val newDy: Double = dy + v.dy
    return Vector2D(newDx, newDy)
  }

  operator fun plus(p: Point2D): Point2D {
    val x: Double = p.x + dx
    val y: Double = p.y + dy
    return Point2D(x, y)
  }

  operator fun unaryMinus(): Vector2D {
    val newDx: Double = -dx
    val newDy: Double = -dy
    return Vector2D(newDx, newDy)
  }

  operator fun minus(v: Vector2D): Vector2D {
    val newDx: Double = dx - v.dx
    val newDy: Double = dy - v.dy
    return Vector2D(newDx, newDy)
  }

  fun scalarProject(target: Vector2D): Double {
    val scalar: Double = this * target
    return scalar / target.magnitude
  }

  fun vectorProject(target: Vector2D): Vector2D {
    val versorProduct: Double = this * target
    return versorProduct * target / (target.magnitude * target.magnitude)
  }
}

operator fun Double.times(v: Vector2D): Vector2D {
  val newDx: Double = this * v.dx
  val newDy: Double = this * v.dy
  return Vector2D(newDx, newDy)
}
