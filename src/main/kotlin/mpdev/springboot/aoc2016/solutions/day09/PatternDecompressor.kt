package mpdev.springboot.aoc2016.solutions.day09

import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import mpdev.springboot.aoc2016.utils.AocException
import org.springframework.stereotype.Component

@Component
class PatternDecompressor(inputDataReader: InputDataReader): PuzzleSolver(inputDataReader, 9) {


    override fun solvePart1(): Int = inputData[0].decompressV1().length

    override fun solvePart2(): Long = inputData[0].decompressedLengthV2()

    companion object {
        fun decompress(s: String, mode: String = "V1"): String {
            var result = ""
            var index = 0
            val regex = Regex("""\((\d+)x(\d+)\)""")
            var matchResult: MatchResult?
            while (regex.find(s, index).also { matchResult = it } != null) {
                val match: MatchResult = matchResult ?: throw AocException("unexpected match error")
                val startMatchIndx = match.groups[0]?.range?.first ?: break
                val endMatchIndx = match.groups[0]?.range?.last ?: break
                val (a, b) = match.destructured
                result += s.substring(index, startMatchIndx)
                when (mode) {
                    "V1" -> repeat(b.toInt()) { result += s.substring(endMatchIndx + 1, endMatchIndx + a.toInt() + 1) }
                    "V2" -> repeat(b.toInt()) { result += decompress(s.substring(endMatchIndx + 1, endMatchIndx + a.toInt() + 1), mode) }
                    else -> throw AocException("invalid decompression mode: $mode")
                }
                index = endMatchIndx + a.toInt() + 1
            }
            return result + s.substring(index)
        }

        fun decompressedV2Length(s: String): Long {
            var result = 0L
            var index = 0
            val regex = Regex("""\((\d+)x(\d+)\)""")
            var matchResult: MatchResult?
            while (regex.find(s, index).also { matchResult = it } != null) {
                val match: MatchResult = matchResult ?: throw AocException("unexpected match error")
                val startMatchIndx = match.groups[0]?.range?.first ?: break
                val endMatchIndx = match.groups[0]?.range?.last ?: break
                val (a, b) = match.destructured
                result += (startMatchIndx - index)
                repeat(b.toInt()) { result += decompressedV2Length(s.substring(endMatchIndx + 1, endMatchIndx + a.toInt() + 1)) }
                index = endMatchIndx + a.toInt() + 1
            }
            return result + (s.length - index)
        }
    }

}

fun String.decompressV1(): String = PatternDecompressor.decompress(this, "V1")
fun String.decompressV2(): String = PatternDecompressor.decompress(this, "V2")
fun String.decompressedLengthV2(): Long = PatternDecompressor.decompressedV2Length(this)

