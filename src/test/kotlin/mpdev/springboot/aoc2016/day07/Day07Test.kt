package mpdev.springboot.aoc2016.day07

import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.day07.IPv7
import mpdev.springboot.aoc2016.solutions.day07.containsAbba
import mpdev.springboot.aoc2016.solutions.day07.containsBab
import mpdev.springboot.aoc2016.solutions.day07.getAbaList
import mpdev.springboot.aoc2016.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day07Test {

    private val day = 7                                     ///////// Update this for a new dayN test
    private lateinit var solver: IPv7                       ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")

    @BeforeEach
    fun setup() {
        solver = IPv7(inputDataReader)
    }

    @Test
    @Order(1)
    fun `Sets Day correctly`() {
        assertThat(solver.day).isEqualTo(day)
    }

    @Test
    @Order(2)
    fun `Reads Input`() {
        solver.addresses.forEach { it.println() }
        assertThat(solver.addresses.size).isEqualTo(5)
    }

    @Test
    @Order(3)
    fun `Checks for ABBA`() {
        assertTrue("abba".containsAbba())
        assertFalse("abcd".containsAbba())
        assertFalse("aaaatyui".containsAbba())
        assertTrue("ioxxoj".containsAbba())
    }

    @Test
    @Order(4)
    fun `Solves Part 1`() {
        val result = solver.solvePart1().also { it.println() }
        assertThat(result).isEqualTo(2)
    }

    @Test
    @Order(4)
    fun `Checks for ABA and BAB`() {
        inputDataReader.testInput = listOf(
            "aba[bab]xyz",
            "xyx[xyx]xyx",
            "aaa[kek]eke",
            "zazbz[bzb]cdb",
            "zazbz[bzx]cdb[uybzbd]grtry"
        )
        val expected = listOf(true, false, true, true, true)
        solver = IPv7(inputDataReader)
        solver.addresses.indices.forEach { i ->
            val addr = solver.addresses[i]
            val abaList = addr.name.map { s -> s.getAbaList() }.flatten().also { it.println() }
            val babFound =
                abaList.map { aba -> addr.specialSequence.any { it.containsBab(aba) } }.any { it }.also { it.println() }
            assertThat(babFound).isEqualTo(expected[i])
        }
    }

    @Test
    @Order(6)
    fun `Solves Part 2`() {
        inputDataReader.testInput = listOf(
            "aba[bab]xyz",
            "xyx[xyx]xyx",
            "aaa[kek]eke",
            "zazbz[bzb]cdb",
            "zazbz[bzx]cdb[uybzbd]grtry"
        )
        solver = IPv7(inputDataReader)
        val result = solver.solvePart2().also { it.println() }
        assertThat(result).isEqualTo(4)
    }
}
