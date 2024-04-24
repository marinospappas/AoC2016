package mpdev.springboot.aoc2016.solutions.day15

import kotlinx.serialization.Serializable
import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import mpdev.springboot.aoc2016.utils.*
import org.springframework.stereotype.Component

@Component
class SpinningDisks(inputDataReader: InputDataReader): PuzzleSolver(inputDataReader, 15) {

    var disks: List<Disk> = InputUtils(AoCInput::class.java).readAoCInput<AoCInput>(inputData)
        .map { Disk(it.id, it.positions, it.start) }

    private fun findCommonSlot(diskList: List<Disk>): Int {
        for (t in 0 .. MAX_TIME)
            if (diskList.all { disk -> disk.isSlotOpen(t + disk.id) })
                return t
        throw AocException("no solution found")
    }

    override fun solvePart1() = findCommonSlot(disks)

    override fun solvePart2() = findCommonSlot(
        disks.toMutableList().also { list -> list.add(Disk(disks.last().id + 1, 11, 0)) }
    )

    companion object {
        const val MAX_TIME = 100_000_000
    }
}

data class Disk(val id: Int, val numPositions: Int, val startPosition: Int) {
    // the disk is at pos.0 (slot open) at times: t = (numPositions - startPosition) + numPositions x N
    //  where N is Integer
    fun isSlotOpen(t: Int) = (t - (numPositions - startPosition)) % numPositions == 0
}

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