package mpdev.springboot.aoc2016.day04

import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.day04.Room
import mpdev.springboot.aoc2016.solutions.day04.RoomValidator
import mpdev.springboot.aoc2016.utils.toFrequency
import mpdev.springboot.aoc2016.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day04Test {

    private val day = 4                                     ///////// Update this for a new dayN test
    private lateinit var solver: RoomValidator             ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")

    @BeforeEach
    fun setup() {
        solver = RoomValidator(inputDataReader)
        solver.initialize()
    }

    @Test
    @Order(1)
    fun `Sets Day correctly`() {
        assertThat(solver.day).isEqualTo(day)
    }

    @Test
    @Order(2)
    fun `Reads Input`() {
        solver.rooms.forEach { it.println() }
        assertThat(solver.rooms.size).isEqualTo(4)
    }

    @Test
    fun `Validates Rooms`() {
        val expected = listOf(true, true, true, false)
        solver.rooms.indices.forEach { i ->
            val room = solver.rooms[i]
            room.name.joinToString("").toFrequency().println()
            assertThat(room.isValid()).isEqualTo(expected[i])
        }
    }

    @Test
    @Order(4)
    fun `Solves Part 1`() {
        val result = solver.solvePart1().also { it.println() }
        assertThat(result).isEqualTo(1514)
    }

    @Test
    @Order(6)
    fun `Deciphers Room Name`() {
        val room = Room(listOf("qzmt","zixmtkozy","ivhz"), 343, "")
        val result = room.decipher().also { it.println() }
        assertThat(result).isEqualTo(listOf("very","encrypted","name"))
    }
}
