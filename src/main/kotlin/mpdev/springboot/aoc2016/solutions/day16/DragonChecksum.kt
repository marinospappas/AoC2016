package mpdev.springboot.aoc2016.solutions.day16

import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.math.min

@Component
class DragonChecksum(inputDataReader: InputDataReader): PuzzleSolver(inputDataReader, 16) {

    fun dragonCurve(input: String, length: Int): String {
        var out = input
        do {
            out = dragonPattern(out)
        } while (out.length < length)
        return out.substring(0, length)
    }

    fun dragonChecksum(a: String): String {
        var chcksm = a
        do {
            chcksm = checkSum(chcksm)
        } while (chcksm.length % 2 == 0)
        return chcksm
    }

    fun checkSum(a: String): String {
        var out = ""
        for (i in a.indices step(2))
            out += if (a[i] == a[i+1]) "1" else "0"
        return out
    }

    fun dragonPattern(a: String): String = "${a}0${a.reversed().flipBits()}"

    override fun solvePart1(): String {
        val data = dragonCurve(inputData[0], inputData[1].toInt())
        log.info("data part 1: {}...", data.substring(0, min(data.length, 100)))
        return dragonChecksum(data)
    }

    override fun solvePart2(): String {
        val data = dragonCurve(inputData[0], 35651584)
        log.info("data part 2: {}...", data.substring(0, 100))
        return dragonChecksum(data)
    }

    fun String.flipBits() = this.toCharArray().map { c -> if (c == '1') '0' else if (c == '0') '1' else c }.joinToString("")
}

