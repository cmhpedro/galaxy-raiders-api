package galaxyraiders.core.game

import galaxyraiders.Config
import galaxyraiders.ports.RandomGenerator
import galaxyraiders.ports.ui.Controller
import galaxyraiders.ports.ui.Controller.PlayerCommand
import galaxyraiders.ports.ui.Visualizer
import kotlin.system.measureTimeMillis
import java.time.Instant
import java.io.File
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode


const val MILLISECONDS_PER_SECOND: Int = 1000

object GameEngineConfig {
  private val config = Config(prefix = "GR__CORE__GAME__GAME_ENGINE__")

  val frameRate = config.get<Int>("FRAME_RATE")
  val spaceFieldWidth = config.get<Int>("SPACEFIELD_WIDTH")
  val spaceFieldHeight = config.get<Int>("SPACEFIELD_HEIGHT")
  val asteroidProbability = config.get<Double>("ASTEROID_PROBABILITY")
  val coefficientRestitution = config.get<Double>("COEFFICIENT_RESTITUTION")

  val msPerFrame: Int = MILLISECONDS_PER_SECOND / this.frameRate
}

@Suppress("TooManyFunctions")
class GameEngine(
  val generator: RandomGenerator,
  val controller: Controller,
  val visualizer: Visualizer,
) {
  val field = SpaceField(
    width = GameEngineConfig.spaceFieldWidth,
    height = GameEngineConfig.spaceFieldHeight,
    generator = generator
  )
  val initTimestamp: Instant

  var playing = true
  var score: Double = 0.0
  var asteroidsDestroyed: Int = 0

  init {
    initTimestamp = Instant.now()
  }

  fun execute() {
    while (true) {
      val duration = measureTimeMillis { this.tick() }

      Thread.sleep(
        maxOf(0, GameEngineConfig.msPerFrame - duration)
      )
    }
  }

  fun execute(maxIterations: Int) {
    repeat(maxIterations) {
      this.tick()
    }
  }

  fun tick() {
    this.processPlayerInput()
    this.updateSpaceObjects()
    this.renderSpaceField()
  }

  fun processPlayerInput() {
    this.controller.nextPlayerCommand()?.also {
      when (it) {
        PlayerCommand.MOVE_SHIP_UP ->
          this.field.ship.boostUp()
        PlayerCommand.MOVE_SHIP_DOWN ->
          this.field.ship.boostDown()
        PlayerCommand.MOVE_SHIP_LEFT ->
          this.field.ship.boostLeft()
        PlayerCommand.MOVE_SHIP_RIGHT ->
          this.field.ship.boostRight()
        PlayerCommand.LAUNCH_MISSILE ->
          this.field.generateMissile()
        PlayerCommand.PAUSE_GAME ->
          this.playing = !this.playing
      }
    }
  }

  fun updateSpaceObjects() {
    if (!this.playing) return
    this.handleCollisions()
    this.moveSpaceObjects()
    this.trimSpaceObjects()
    this.generateAsteroids()
  }

  fun handleCollisions() {
    this.field.spaceObjects.forEachPair {
        (first, second) ->
      if (first.impacts(second)) {
        first.collideWith(second, GameEngineConfig.coefficientRestitution)
        if (first is Missile && second is Asteroid) {
          this.field.generateExplosion(second)
          this.score += 100/second.radius
          this.asteroidsDestroyed++
          this.saveScore()
        }
      }
    }
  }

  fun moveSpaceObjects() {
    this.field.moveShip()
    this.field.moveAsteroids()
    this.field.moveMissiles()
  }

  fun trimSpaceObjects() {
    this.field.trimAsteroids()
    this.field.trimMissiles()
    this.field.trimExplosions()
  }

  fun generateAsteroids() {
    val probability = generator.generateProbability()

    if (probability <= GameEngineConfig.asteroidProbability) {
      this.field.generateAsteroid()
    }
  }

  fun renderSpaceField() {
    this.visualizer.renderSpaceField(this.field)
  }

  fun saveScore() {
    val timestamp = this.initTimestamp.toString()
    val informations = """{ "score": ${this.score}, "asteroids destroyed": ${this.asteroidsDestroyed} }"""

    val mapper = ObjectMapper()
    val informationsJson = mapper.readTree(informations)

    val scoreboardFile = File("src/main/kotlin/galaxyraiders/core/score/Scoreboard.json")
    val leaderboardFile = File("src/main/kotlin/galaxyraiders/core/score/Leaderboard.json")

    val scoreboardJson = convertFileToJson(scoreboardFile)
    val leaderboardJson = convertFileToJson(leaderboardFile)

    (scoreboardJson as? ObjectNode)?.set<JsonNode>(timestamp, informationsJson)
    (leaderboardJson as? ObjectNode)?.set<JsonNode>(timestamp, informationsJson)
    keepTop3(leaderboardJson)

    scoreboardFile.writeText(scoreboardJson.toPrettyString())
    leaderboardFile.writeText(leaderboardJson.toPrettyString())
  }
}

fun <T> List<T>.forEachPair(action: (Pair<T, T>) -> Unit) {
  for (i in 0 until this.size) {
    for (j in i + 1 until this.size) {
      action(Pair(this[i], this[j]))
    }
  }
}

fun convertFileToJson(file: File): JsonNode {
  if (!file.exists()) {
    file.createNewFile()
  }

  if (file.readText().isEmpty()) {
    file.writeText("{}")
  }

  val mapper = ObjectMapper()
  val json = mapper.readTree(file.readText())
  return json
}

fun keepTop3(json: JsonNode) {
  val sorted = json.fields().asSequence().sortedByDescending { it.value.get("score").asDouble() }.toList()
  val top3 = sorted.take(3)

  (json as? ObjectNode)?.removeAll()
  top3.forEach { (timestamp, informations) ->
    (json as? ObjectNode)?.set<JsonNode>(timestamp, informations)
  }
}
