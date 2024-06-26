package mpdev.springboot.aoc2016.solutions.day05

import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import mpdev.springboot.aoc2016.utils.Md5
import org.springframework.stereotype.Component
import kotlin.experimental.and

@Component
class PasswordDecoder(inputDataReader: InputDataReader): PuzzleSolver(inputDataReader, 5) {

    var startId = inputData[0]

    override fun solvePart1(): String {
        return generateSequence(0) { it + 1 }
            .map { Md5.checksum("$startId$it") }
            .filter { it[0] == byte0 && it[1] == byte0 && (it[2] and 0xf0.toByte()) == byte0 }
            .map { String.format("%x", it[2] and 0x0f.toByte()) }
            .take(8)
            .joinToString("")
    }

    override fun solvePart2(): String {
        return generateSequence(0) { it + 1 }
            .map { Md5.checksum("$startId$it") }
            .filter { it[0] == byte0 && it[1] == byte0 && (it[2] and 0xf0.toByte()) == byte0 && (it[2] and 0x0f.toByte()) <= 7.toByte() }
            .map { Pair(it[2].toInt() and 0x0f, String.format("%x", (it[3].toInt() and 0xf0) shr 4).first()) }
            .distinctBy { it.first }
            .take(8)
            .sortedBy { it.first }
            .map { it.second }
            .joinToString("")
    }

    companion object {
        const val byte0 = 0.toByte()
    }
}
