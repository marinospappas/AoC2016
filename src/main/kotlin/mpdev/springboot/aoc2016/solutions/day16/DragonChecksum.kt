package mpdev.springboot.aoc2016.solutions.day16

import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import mpdev.springboot.aoc2016.utils.AocException
import org.springframework.stereotype.Component
import kotlin.math.min

@Component
class DragonChecksum(inputDataReader: InputDataReader): PuzzleSolver(inputDataReader, 16) {

    override fun initialize() {
        for (i in 0 until 66536) {
            val data = i.toString(2).padStart(16, '0')
            CHECKSUM_16[data] = dragonChecksum(data)
        }
        println()
    }

    fun dragonCurve(input: String, length: Int): String {
        var out = input
        do {
            out = dragonPattern(out)
        } while (out.length < length)
        return out.substring(0, length)
    }

    fun dragonPattern(a: String): String = "${a}0${a.reversed().flipBits()}"

    fun checkSum(a: String): String {
        if (a.any { !it.isDigit() }) throw AocException("invalid checksum [$a]")
        var out = ""
        for (i in 0 .. a.lastIndex-1 step(2))
            out += "${a[i].digitToInt().xor(a[i+1].digitToInt()).inv().and(1)}"
        return out
    }

    fun dragonChecksum(a: String): String {
        var chcksm = a
        do {
            chcksm = checkSum(chcksm)
        } while (chcksm.length % 2 == 0)
        return chcksm
    }

    fun checkSum16(a: String): String {
        var out = ""
        for (i in 0 until a.length / 16)
            out += "${CHECKSUM_16[a.substring(i * 16, (i + 1) * 16)]}"
        return out
    }

    fun checkSum256(a: String): String {
        var out = ""
        for (i in 0 until a.length / 256)
            out += checkSum16(a.substring(i * 256, (i+1) * 256))
        return out
    }

    fun checkSum65536(a: String): String {
        var out = ""
        for (i in 0 until a.length / 65536)
            out += checkSum256(a.substring(i * 65536, (i+1) * 65536))
        return out
    }

    override fun solvePart1(): String {
        val data = dragonCurve(inputData[0], inputData[1].toInt())
        log.info("data part 1: {}...", data.substring(0, min(data.length, 100)))
        return dragonChecksum(data)
    }

    override fun solvePart2(): String {
        val data = dragonCurve(inputData[0], 35_651_584)
        log.info("data part 2: {}...", data.substring(0, 100))
        val checksum1 = checkSum65536(data)
        return dragonChecksum(checksum1)
    }

    fun String.flipBits() = this.toCharArray().map { c -> if (c == '1') '0' else if (c == '0') '1' else c }.joinToString("")

    companion object {
        val CHECKSUM_16 = mutableMapOf<String, String>()
    }
}

