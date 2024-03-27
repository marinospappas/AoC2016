package mpdev.springboot.aoc2016.solutions.day01

import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import mpdev.springboot.aoc2016.utils.AocException
import mpdev.springboot.aoc2016.utils.GridUtils
import mpdev.springboot.aoc2016.utils.Point
import org.springframework.stereotype.Component

@Component
class DistanceCalculator(inputDataReader: InputDataReader): PuzzleSolver(inputDataReader, 1) {

    companion object {
        val START_HEADING = GridUtils.Direction.UP
        val START_POSITION = Point(0,0)

    }

    lateinit var directions: List<Step>

    override fun initialize() {
        directions = inputData[0].split(Regex(", *")).map { x -> Step(GridUtils.Direction.of(x[0]), x.substring(1).toInt()) }
    }

    fun findNewPosition(): Point {
        var curHeading = START_HEADING
        return directions.fold(START_POSITION) { acc, dir ->
            curHeading = curHeading.turn(dir.turnDir)
            acc + curHeading.increment.times(dir.steps)
        }
    }

    fun findPositionVisitedTwice(): Point {
        var curHeading = START_HEADING
        var curPosition = START_POSITION
        val positions = mutableSetOf(START_POSITION)
        for (dir in directions) {
            curHeading = curHeading.turn(dir.turnDir)
            for (i in 0 until dir.steps) {
                curPosition += curHeading.increment
                if (positions.contains(curPosition))
                    return curPosition
                else
                    positions.add(curPosition)
            }
        }
        throw AocException("could not find any position visited twice")
    }

    override fun solvePart1(): Int {
        val newPosition = findNewPosition()
        return newPosition.manhattan(START_POSITION)
    }

    override fun solvePart2(): Int {
        val newPosition = findPositionVisitedTwice()
        return newPosition.manhattan(START_POSITION)
    }

}

data class Step(val turnDir: GridUtils.Direction, val steps: Int)