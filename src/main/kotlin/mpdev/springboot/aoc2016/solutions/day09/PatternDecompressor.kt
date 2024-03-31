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
        private val regex = Regex("""\((\d+)x(\d+)\)""")

        fun decompress(s: String, mode: String = "V1"): String {
            var result = ""
            var index = 0
            var matchResult: MatchResult?
            while (regex.find(s, index).also { matchResult = it } != null) {
                val match: MatchResult = matchResult ?: throw AocException("unexpected match error")
                val startMarkerIndx = match.groups[0]?.range?.first ?: break
                val endMarkerIndx = match.groups[0]?.range?.last ?: break
                val (aMarker, bMarker) = match.destructured
                result += s.substring(index, startMarkerIndx)
                when (mode) {
                    "V1" -> repeat(bMarker.toInt()) { result += s.substring(endMarkerIndx + 1, endMarkerIndx + aMarker.toInt() + 1) }
                    "V2" -> repeat(bMarker.toInt()) { result += decompress(s.substring(endMarkerIndx + 1, endMarkerIndx + aMarker.toInt() + 1), mode) }
                    else -> throw AocException("invalid decompression mode: $mode")
                }
                index = endMarkerIndx + aMarker.toInt() + 1
            }
            return result + s.substring(index)
        }

        fun decompressedV2Length(s: String): Long {
            var result = 0L
            var index = 0
            var matchResult: MatchResult?
            while (regex.find(s, index).also { matchResult = it } != null) {
                val match: MatchResult = matchResult ?: throw AocException("unexpected match error")
                val startMarkerIndx = match.groups[0]?.range?.first ?: break
                val endMarkerIndx = match.groups[0]?.range?.last ?: break
                val (aMarker, bMarker) = match.destructured
                result += (startMarkerIndx - index)
                result += bMarker.toInt() * decompressedV2Length(s.substring(endMarkerIndx + 1, endMarkerIndx + aMarker.toInt() + 1))
                index = endMarkerIndx + aMarker.toInt() + 1
            }
            return result + (s.length - index)
        }
    }
}

fun String.decompressV1(): String = PatternDecompressor.decompress(this, "V1")
fun String.decompressV2(): String = PatternDecompressor.decompress(this, "V2")
fun String.decompressedLengthV2(): Long = PatternDecompressor.decompressedV2Length(this)

