package mpdev.springboot.aoc2016.solutions.day05

import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import mpdev.springboot.aoc2016.utils.AocException
import mpdev.springboot.aoc2016.utils.Md5
import org.springframework.stereotype.Component
import kotlin.experimental.and

@Component
class PasswordDecoder(inputDataReader: InputDataReader): PuzzleSolver(inputDataReader, 5) {

    var startId = inputData[0]
    val md5 = Md5()

    override fun solvePart1(): String {
        return generateSequence(0) { it + 1 }
            .map { md5.checksum("$startId$it") }
            .filter { it[0] == byte0 && it[1] == byte0 && (it[2] and 0xf0.toByte()) == byte0 }
            .map { String.format("%x", it[2] and 0x0f.toByte()) }
            .take(8)
            .toList()
            .joinToString("")
    }

    override fun solvePart2(): String {
        val result = "........".toMutableList()
        for (i in 0..Int.MAX_VALUE) {
            val md5sum = md5.checksum("$startId$i")
            if (md5sum[0] == byte0 && md5sum[1] == byte0 && (md5sum[2] and 0xf0.toByte()) == byte0
                && (md5sum[2] and 0x0f.toByte()) <= 7.toByte()) {
                val index = md5sum[2].toInt() and 0x7
                if (result[index] == '.')
                    result[index] = String.format("%x", (md5sum[3].toInt() and 0xf0) shr 4).first()
                if (result.none { it == '.' })
                    return result.joinToString("")
            }
        }
        throw AocException("Did not identify 8-character code")
    }

    companion object {
        const val byte0 = 0.toByte()
    }
}
