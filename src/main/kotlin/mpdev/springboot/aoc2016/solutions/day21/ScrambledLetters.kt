package mpdev.springboot.aoc2016.solutions.day21

import kotlinx.serialization.Serializable
import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import mpdev.springboot.aoc2016.utils.*
import org.springframework.stereotype.Component

@Component
class ScrambledLetters(inputDataReader: InputDataReader): PuzzleSolver(inputDataReader, 21) {

    var inputString = "abcdefgh"
    var inputString2 = "fbgdceah"
    final val instructionList: List<AoCInput> = InputUtils(AoCInput::class.java).readAoCInput(inputData)

    override fun solvePart1(): String {
        var result = inputString.toList()
        instructionList.forEach { instr -> result = instr.instruction.execute(result, instr.param1, instr.param2) }
        return result.joinToString("")
    }

    override fun solvePart2(): String {
        var result = inputString2.toList()
        instructionList.reversed().forEach { instr -> result = instr.instruction.reverse(result, instr.param1, instr.param2) }
        return result.joinToString("")
    }
}

fun List<Char>.swapPosition(pos1: String, pos2: String): List<Char> {
    val l = this.toMutableList()
    l[pos1.toInt()] = this[pos2.toInt()]
    l[pos2.toInt()] = this[pos1.toInt()]
    return l
}
fun List<Char>.swapLetter(let1: String, let2: String): List<Char> {
    val l = this.toMutableList()
    l.indices.forEach { i ->
        if (this[i] == let1.first()) l[i] = let2.first()
        if (this[i] == let2.first()) l[i] = let1.first()
    }
    return l
}
fun List<Char>.reversePositions(pos1: String, pos2: String): List<Char> {
    val l = this.toMutableList()
    val p1 = pos1.toInt()
    val p2 = pos2.toInt()
    return l.subList(0, p1) + l.subList(p1, p2+1).reversed() +
            if (p2 == l.lastIndex) emptyList() else l.subList(p2+1, l.size)
}
fun List<Char>.rotateRight(n: String): List<Char> {
    val l = CircularList(this.toMutableList())
    l.rotateRight(n.toInt())
    return l.toList()
}
fun List<Char>.rotateLeft(n: String): List<Char> {
    val l = CircularList(this.toMutableList())
    l.rotateLeft(n.toInt())
    return l.toList()
}
fun List<Char>.rotateBasedOnPositionOfLetter(a: String): List<Char> {
    val l = CircularList(this.toMutableList())
    var n = this.indexOf(a.first()) + 1
    if (n > 4) ++n
    l.rotateRight(n)
    return l.toList()
}
fun List<Char>.movePosition(pos1: String, pos2: String): List<Char> {
    var l = this.toMutableList()
    val c = l.removeAt(pos1.toInt())
    val p2 = pos2.toInt()
    if (p2 > l.lastIndex) l.add(c) else l = (l.subList(0, p2) + listOf(c) + l.subList(p2, l.size)).toMutableList()
    return l
}
fun List<Char>.reverseRotateBasedOnPositionOfLetter(a: String): List<Char> {
    var l = this
    for (i in 0 .. size + 2) {
        l = l.rotateLeft("1")
        if (l.rotateBasedOnPositionOfLetter(a) == this)
            return l
    }
    throw AocException("the operation Rotate Based on Position of Letter [$a] cannot be reversed on string $this")
}

enum class Instruction(val value: String,
                       val execute: (List<Char>, String, String) -> List<Char>,
                       val reverse: (List<Char>, String, String) -> List<Char>) {
    SWP_POS("swap position", { l, a, b -> l.swapPosition(a, b) }, { l, a, b -> l.swapPosition(b, a) }),
    SWP_LTR("swap letter", { l, a, b -> l.swapLetter(a, b) }, { l, a, b -> l.swapLetter(a, b) }),
    REV_POS("reverse positions", { l, a, b -> l.reversePositions(a, b) }, { l, a, b -> l.reversePositions(a, b) }),
    ROT_RGT("rotate right", { l, a, _ -> l.rotateRight(a) }, { l, a, _ -> l.rotateLeft(a) }),
    ROT_LFT("rotate left", { l, a, _ -> l.rotateLeft(a) }, { l, a, _ -> l.rotateRight(a) }),
    MOV_POS("move position", { l, a, b -> l.movePosition(a, b) }, { l, a, b -> l.movePosition(b, a) }),
    ROT_PLR("rotate based on position of letter", { l, a, _ -> l.rotateBasedOnPositionOfLetter(a) },
        { l, a, _ -> l.reverseRotateBasedOnPositionOfLetter(a) }),
    NOP("NO_OPERATION", { _, _, _ -> emptyList() }, { _, _, _ -> emptyList() });

    companion object {
        @JvmStatic
        fun fromString(s: String): Instruction = Instruction.values().find { it.value == s } ?:
                throw AocException("Instruction [$s] not found")
    }
}

@Serializable
@AocInClass(delimiters = [","])
@AocInReplacePatterns([" to position ", ",",  "move position ", "move position,",
    """ steps?$""", "", " with position ", ",", "swap letter ", "swap letter,", " with letter ", ",",
    "swap position ", "swap position,", " through ", ",",
    "reverse positions ", "reverse positions,", "rotate left ", "rotate left,", "rotate right ", "rotate right,",
    "rotate based on position of letter ", "rotate based on position of letter,",
    """$""", ","
])
data class AoCInput(
    // move position 6 to position 5
    // 0             1             2
    @AocInField(0) val instruction: Instruction,
    @AocInField(1) val param1: String,
    @AocInField(2) val param2: String
)

