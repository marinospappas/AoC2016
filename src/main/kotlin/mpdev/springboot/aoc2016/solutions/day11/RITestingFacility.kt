package mpdev.springboot.aoc2016.solutions.day11

import kotlinx.serialization.Serializable
import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import mpdev.springboot.aoc2016.utils.*
import org.springframework.stereotype.Component
import java.util.*

@Component
class RITestingFacility(inputDataReader: InputDataReader): PuzzleSolver(inputDataReader, 11) {

    override fun initialize() {
        val aocInputList1: List<AoCInput> = InputUtils(AoCInput::class.java).readAoCInput(inputData)
    }

    override fun solvePart1(): Int  = 0

    override fun solvePart2(): Int = 0

    companion object {
    }
}

@Serializable
@AocInClass(delimiters = [" +"])
@AocInReplacePatterns(["goes to", "", "gives low to", "", "and high to", "", """$""", " -1 -1 -1"])
data class AoCInput(
    // value 5 goes to bot 2
    // 0     1         2   3
    // bot 2 gives low to bot 1 and high to bot 0
    // 0   1              2   3             4   5
    @AocInField(0) val recId: String,
    @AocInField(1) val id: Int,
    @AocInField(2) val dest1: String,
    @AocInField(2) val dest1Id: Int,
    @AocInField(3) val dest2: String,
    @AocInField(3) val dest2Id: Int
)
