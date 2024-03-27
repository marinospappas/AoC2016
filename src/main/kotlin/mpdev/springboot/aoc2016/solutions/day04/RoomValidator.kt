package mpdev.springboot.aoc2016.solutions.day04

import kotlinx.serialization.Serializable
import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import mpdev.springboot.aoc2016.solutions.day04.RoomValidator.Companion.alphabet
import mpdev.springboot.aoc2016.utils.*
import org.springframework.stereotype.Component

@Component
class RoomValidator(inputDataReader: InputDataReader): PuzzleSolver(inputDataReader, 4) {

    lateinit var rooms: List<Room>

    override fun initialize() {
        val aocInputList: List<AoCInput> = InputUtils(AoCInput::class.java).readAoCInput(inputData)
        rooms = aocInputList.map { Room(it.id.subList(0, it.id.lastIndex), it.id.last().toInt(), it.chksum) }
    }

    override fun solvePart1(): Int = rooms.filter { it.isValid() }.sumOf { it.sectorId }

    override fun solvePart2(): Int = rooms.first { it.isValid() && it.decipher().contains("northpole") }.sectorId

    companion object {
        val alphabet = CircularList(('a'..'z').toMutableList())
    }
}

fun Char.decode(n : Int): Char = alphabet[alphabet.indexOf(this) + n]

data class Room(val name: List<String>, val sectorId: Int, val chksum: String) {
    fun isValid(): Boolean = name.joinToString("").toFrequency().startsWith(chksum)
    fun decipher() = name.map { it.toList().map { c -> c.decode(sectorId) }.joinToString("") }
}

@Serializable
@AocInClass(delimiters = ["\\["])
@AocInRemovePatterns(["]"])
data class AoCInput(
    // aaaaa-bbb-z-y-x-123[abxyz]
    // 0                   1
    @AocInField(0, delimiters = ["-"]) val id: List<String>,
    @AocInField(1) val chksum: String,
)
