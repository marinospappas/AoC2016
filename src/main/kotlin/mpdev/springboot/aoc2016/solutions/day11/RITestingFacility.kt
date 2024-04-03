package mpdev.springboot.aoc2016.solutions.day11

import kotlinx.serialization.Serializable
import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import mpdev.springboot.aoc2016.utils.*
import org.springframework.stereotype.Component

@Component
class RITestingFacility(inputDataReader: InputDataReader): PuzzleSolver(inputDataReader, 11) {

    override fun initialize() {
        val aocInputList1: List<AoCInput> = InputUtils(AoCInput::class.java).readAoCInput(inputData)
        endState = listOf(setOf(), setOf(), setOf(), (generators + microchips + 'E').toSet())
    }

    override fun solvePart1(): Int  = 0

    override fun solvePart2(): Int = 0

    companion object {
        var generators = Generators.values().toSet()
        var microchips = MicroChips.values().toSet()
        lateinit var endState: List<Set<Any>>
    }
}

enum class Generators {
    C, P, R, S, T
}

enum class MicroChips {
    c, p, r, s, t
}

@Serializable
@AocInClass(delimiters = [" +"])
@AocInReplacePatterns(["goes to", "", "gives low to", "", "and high to", "", """$""", " -1 -1 -1"])
data class AoCInput(
    // ...
    @AocInField(0) val recId: String,
    @AocInField(1) val id: Int,
    @AocInField(2) val dest1: String,
    @AocInField(2) val dest1Id: Int,
    @AocInField(3) val dest2: String,
    @AocInField(3) val dest2Id: Int
)
