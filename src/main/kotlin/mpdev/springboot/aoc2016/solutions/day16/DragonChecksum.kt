package mpdev.springboot.aoc2016.solutions.day16

import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import org.springframework.stereotype.Component

@Component
class DragonChecksum(inputDataReader: InputDataReader): PuzzleSolver(inputDataReader, 16) {

    fun dragonCurve(input: String, length: Int): String {
        var out = input
        do {
            out = dragonPattern(out)
        } while (out.length < length)
        return out.substring(0, length)
    }

    fun dragonPattern(a: String): String = "${a}0${a.reversed().flipBits()}"

    override fun solvePart1() = 0

    override fun solvePart2() = 0

    fun String.flipBits() = this.toCharArray().map { c -> if (c == '1') '0' else if (c == '0') '1' else c }.joinToString("")

    companion object {
    }
}

