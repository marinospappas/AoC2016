package mpdev.springboot.aoc2016.solutions.day21

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import mpdev.springboot.aoc2016.utils.*
import org.springframework.stereotype.Component

@Component
class ScrambledLetters(inputDataReader: InputDataReader): PuzzleSolver(inputDataReader, 21) {

    var inputString = "abcde"
    final val instructionList: List<AoCInput> = InputUtils(AoCInput::class.java).readAoCInput(inputData)


    override fun solvePart1(): String {
        return ""
    }

    override fun solvePart2() = 0

    companion object {
    }
}

enum class Instruction(val value: String) {
    MOV_POS("move position"),
    ROT_RGT("rotate right"),
    ROT_LFT("rotate left"),
    ROT_POS_OF_LTR("rotate based on position of letter"),
    SWP_LTR("swap letter"),
    REV_POS("reverse positions"),
    SWP_POS("swap position"),
    REV_POSNS("reverse positions"),
    NOP("NO_OPERATION");
    companion object {
        @JvmStatic
        fun fromString(s: String) = Instruction.values().find { it.value == s } ?: NOP
    }
}

@Serializable
@AocInClass(delimiters = [","])
@AocInReplacePatterns([" to position ", ",",  "move position ", "move position,",
    """ steps?$""", "", " with position ", ",", "swap letter ", "swap letter,", " with letter ", ",",
    "swap position ", "swap position,", "swap position ", "swap position,", " through ", ",",
    "reverse positions ", "reverse positions,", "rotate left ", "rotate left,", "rotate right ", "rotate right,",
    "rotate based on position of letter ", "rotate based on position of letter,",
    """$""", ","
])
data class AoCInput(
    // move position 6 to position 5
    // 0             1             2
    @AocInField(0) val instruction: Instruction,
    @AocInField(1) @Contextual val param1: String,
    @AocInField(2) @Contextual val param2: String
)

