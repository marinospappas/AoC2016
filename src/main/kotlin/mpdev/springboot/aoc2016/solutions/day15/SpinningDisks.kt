package mpdev.springboot.aoc2016.solutions.day15

import kotlinx.serialization.Serializable
import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import mpdev.springboot.aoc2016.utils.AocInClass
import mpdev.springboot.aoc2016.utils.AocInField
import mpdev.springboot.aoc2016.utils.AocInReplacePatterns
import mpdev.springboot.aoc2016.utils.InputUtils
import org.springframework.stereotype.Component

@Component
class SpinningDisks(inputDataReader: InputDataReader): PuzzleSolver(inputDataReader, 15) {

    var disks: List<Disk> = InputUtils(AoCInput::class.java).readAoCInput<AoCInput>(inputData)
        .map { Disk(it.id, it.positions, it.start) }

    override fun initialize() {
    }

    override fun solvePart1(): Int {
        return 0
    }

    override fun solvePart2(): Int {
        return 0
    }

    companion object {
    }
}

data class Disk(val id: Int, val numPositions: Int, val startPosition: Int)

@Serializable
@AocInClass(delimiters = [","])
@AocInReplacePatterns(["""Disc #""", "", " has", ",", " positions; at time=0, it is at position", ",", """\.$""", ""])
data class AoCInput(
    // Disc #1 has 5 positions; at time=0, it is at position 4.
    // 0     1         2   3
    @AocInField(0) val id: Int,
    @AocInField(1) val positions: Int,
    @AocInField(2) val start: Int
)