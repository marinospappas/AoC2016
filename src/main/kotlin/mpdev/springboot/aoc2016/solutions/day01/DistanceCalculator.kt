package mpdev.springboot.aoc2016.solutions.day01

import mpdev.springboot.aoc2016.utils.AocException
import mpdev.springboot.aoc2016.utils.GridUtils
import mpdev.springboot.aoc2016.utils.Point

class DistanceCalculator(input: List<String>) {

    companion object {
        val START_HEADING = GridUtils.Direction.UP
        val START_POSITION = Point(0,0)

    }

    val directions: List<Step> = input[0].split(Regex(", *"))
        .map { x -> Step(
            GridUtils.Direction.of(x[0]),
            x.substring(1).toInt()
        ) }

    fun findNewPosition(): Point {
        var curHeading = START_HEADING
        var curPosition = START_POSITION
        directions.forEach { dir ->
            curHeading = curHeading.turn(dir.turnDir)
            curPosition += curHeading.increment.times(dir.steps)
        }
        return curPosition
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
}

data class Step(val turnDir: GridUtils.Direction, val steps: Int)