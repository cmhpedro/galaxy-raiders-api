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
    get() = Vector2D(dy/magnitude, -dx/magnitude)

  operator fun times(scalar: Double): Vector2D {
    val NewDx: Double = dx * scalar
    val NewDy: Double = dy * scalar
    return Vector2D(NewDx, NewDy)
  }

  operator fun div(scalar: Double): Vector2D {
    val NewDx: Double = dx / scalar
    val NewDy: Double = dy / scalar
    return Vector2D(NewDx, NewDy)
  }

  operator fun times(v: Vector2D): Double {
    val x: Double = dx * v.dx
    val y: Double = dy * v.dy
    return x + y
  }

  operator fun plus(v: Vector2D): Vector2D {
    val NewDx: Double = dx + v.dx
    val NewDy: Double = dy + v.dy
    return Vector2D(NewDx, NewDy)
  }

  operator fun plus(p: Point2D): Point2D {
    val x: Double = p.x + dx
    val y: Double = p.y + dy
    return Point2D(x, y)
  }

  operator fun unaryMinus(): Vector2D {
    val NewDx: Double = -dx
    val NewDy: Double = -dy
    return Vector2D(NewDx, NewDy)
  }

  operator fun minus(v: Vector2D): Vector2D {
    val NewDx: Double = dx - v.dx
    val NewDy: Double = dy - v.dy
    return Vector2D(NewDx, NewDy)
  }

  fun scalarProject(target: Vector2D): Double {
    val scalar: Double = this * target
    return scalar / target.magnitude
  }

  fun vectorProject(target: Vector2D): Vector2D {
    val scalar: Double = this * target / (target.magnitude * target.magnitude)
    return scalar * target
  }
}

operator fun Double.times(v: Vector2D): Vector2D {
  val NewDx: Double = this * v.dx
  val NewDy: Double = this * v.dy
  return Vector2D(NewDx, NewDy)
}
