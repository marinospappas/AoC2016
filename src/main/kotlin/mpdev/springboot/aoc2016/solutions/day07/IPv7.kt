package mpdev.springboot.aoc2016.solutions.day07

import kotlinx.serialization.Serializable
import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import mpdev.springboot.aoc2016.utils.*
import org.springframework.stereotype.Component

@Component
class IPv7(inputDataReader: InputDataReader): PuzzleSolver(inputDataReader, 7) {

    lateinit var addresses: List<IPV7Address>

    override fun initialize() {
        val aocInputList: List<AoCInput> = InputUtils(AoCInput::class.java).readAoCInput(inputData)
        addresses = aocInputList.map { rec -> IPV7Address(
            rec.name.indices.filter { it % 2 == 0 }.map { indx -> rec.name[indx] },
            rec.name.indices.filter { it % 2 == 1 }.map { indx -> rec.name[indx] }
        ) }
    }

    override fun solvePart1(): Int = addresses.count { it.supportsTls() }

    override fun solvePart2(): Int = addresses.count { it.supportsSsl() }

}

fun String.isAbba(): Boolean {
    val chars = this.toCharArray()
    return chars.size >= 4 && chars[1] != chars[0] && chars[1] == chars[2] && chars[0] == chars[3]
}
fun String.isAba(): Boolean {
    val chars = this.toCharArray()
    return chars.size >= 3 && chars[1] != chars[0] && chars[0] == chars[2]
}

fun String.containsAbba(): Boolean = this.indices.any { this.substring(it).isAbba() }
fun String.getAbaList(): List<String> {
    val result = mutableListOf<String>()
    this.indices.forEach { if (this.substring(it).isAba()) result.add(this.substring(it, it+3)) }
    return result
}
fun String.containsBab(aba: String): Boolean {
    val abaChars = aba.toCharArray()
    val bab = charArrayOf(abaChars[1], abaChars[0], abaChars[1]).joinToString("")
    return this.contains(bab)
}


data class IPV7Address(val name: List<String>, val specialSequence: List<String>) {
    fun supportsTls(): Boolean = name.any { it.containsAbba() } && specialSequence.none { it.containsAbba() }
    fun supportsSsl(): Boolean = name.map { s ->
        s.getAbaList().any { aba -> specialSequence.any { it.containsBab(aba) } }
    }.any { it }
}

@Serializable
@AocInClass
@AocInReplacePatterns(patterns = ["\\[", ",", "]", ","])
data class AoCInput(
    // xuabbx[tgetfqp]cbff[vwjej]ejeros
    // 0
    @AocInField(0, delimiters = [","], listType = [ListType.string]) val name: List<String>,
)
