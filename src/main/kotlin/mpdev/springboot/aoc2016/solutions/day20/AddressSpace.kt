package mpdev.springboot.aoc2016.solutions.day20

import kotlinx.serialization.Serializable
import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import mpdev.springboot.aoc2016.utils.*
import org.springframework.stereotype.Component

@Component
class AddressSpace(inputDataReader: InputDataReader): PuzzleSolver(inputDataReader, 20) {

    private final val aocInputList: List<AoCInput> = InputUtils(AoCInput::class.java).readAoCInput(inputData)
    val addressRanges = aocInputList.map { LongRange(it.first, it.second) }.sortedBy { it.first }
    val normalisedRanges = mutableListOf<LongRange>()
    val allowedRanges = mutableListOf<LongRange>()

    fun normaliseAddressRanges() {
        normalisedRanges.clear()
        normalisedRanges.add(addressRanges[0])
        for (i in 1 .. addressRanges.lastIndex) {
            val prevRange = normalisedRanges.last()
            val newRange = addressRanges[i]
            if (newRange.first > prevRange.last + 1)        // non-overlapping ranges - add new range
                normalisedRanges.add(newRange)
            else if (newRange.first <= prevRange.last + 1 && newRange.last > prevRange.last)    // overlapping ranges - combine
                normalisedRanges[normalisedRanges.lastIndex] = LongRange(prevRange.first, newRange.last)
                                                        // else new range completely inside prev.range - ignore
        }
    }

    fun getAllowedAddresses() {
        allowedRanges.clear()
        if (normalisedRanges.first().first > ADDRESS_RANGE.first)
            allowedRanges.add(LongRange(ADDRESS_RANGE.first, normalisedRanges.first().first - 1))
        for (i in 0 .. normalisedRanges.lastIndex - 1)
            allowedRanges.add(LongRange(normalisedRanges[i].last + 1, normalisedRanges[i+1].first - 1))
        if (normalisedRanges.last().last < ADDRESS_RANGE.last)
            allowedRanges.add(LongRange(normalisedRanges.last().last + 1, ADDRESS_RANGE.last))
    }

    override fun solvePart1(): Long {
        normaliseAddressRanges()
        getAllowedAddresses()
        return allowedRanges.first().first
    }

    override fun solvePart2() = allowedRanges.sumOf { it.last - it.first + 1 }

    companion object {
        var ADDRESS_RANGE = LongRange(0L, 0xFFFFFFFF)
    }
}


@Serializable
@AocInClass(delimiters = ["-"])
data class AoCInput(
    // 2643879391-2646512593
    // 0          1
    @AocInField(0) val first: Long,
    @AocInField(1) val second: Long
)

