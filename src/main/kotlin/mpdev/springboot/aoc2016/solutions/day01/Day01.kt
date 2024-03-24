package mpdev.springboot.aoc2016.solutions.day01

import mpdev.springboot.aoc2016.model.PuzzlePartSolution
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureNanoTime

@Component
class Day01: PuzzleSolver() {

    final override fun setDay() {
        day = 1
    }

    init {
        setDay()
    }

    var result = 0
    lateinit var distanceCalculator: DistanceCalculator

    override fun initSolver(): Pair<Long,String> {
        result = 0
        val elapsed = measureNanoTime {
            distanceCalculator = DistanceCalculator(inputData)
        }
        return Pair(elapsed/1000, "micro-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureNanoTime {
            val newPosition = distanceCalculator.findNewPosition()
            result = newPosition.manhattan(DistanceCalculator.START_POSITION)
        }
        return PuzzlePartSolution(1, result.toString(), elapsed/1000, "micro-sec")
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureNanoTime {
            val newPosition = distanceCalculator.findPositionVisitedTwice()
            result = newPosition.manhattan(DistanceCalculator.START_POSITION)
        }
        return PuzzlePartSolution(2, result.toString(), elapsed/1000, "micro-sec")
    }

}