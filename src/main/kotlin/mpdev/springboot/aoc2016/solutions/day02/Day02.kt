package mpdev.springboot.aoc2016.solutions.day02

import mpdev.springboot.aoc2016.model.PuzzlePartSolution
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.system.measureNanoTime

@Component
class Day02: PuzzleSolver() {

    final override fun setDay() {
        day = 2
    }

    init {
        setDay()
    }

    var result = ""
    lateinit var keypadDecoder: KeypadDecoder

    override fun initSolver(): Pair<Long,String> {
        result = ""
        val elapsed = measureNanoTime {
            keypadDecoder = KeypadDecoder(inputData)
        }
        return Pair(elapsed/1000, "micro-sec")
    }

    override fun solvePart1(): PuzzlePartSolution {
        val elapsed = measureNanoTime {
            result = keypadDecoder.findCode(KeypadDecoder.keypad1)
        }
        return PuzzlePartSolution(1, result, elapsed/1000, "micro-sec")
    }

    override fun solvePart2(): PuzzlePartSolution {
        val elapsed = measureNanoTime {
            result = keypadDecoder.findCode(KeypadDecoder.keypad2)
        }
        return PuzzlePartSolution(2, result, elapsed/1000, "micro-sec")
    }

}