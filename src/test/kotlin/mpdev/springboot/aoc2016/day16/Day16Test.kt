package mpdev.springboot.aoc2016.day16

import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.day16.DragonChecksum
import mpdev.springboot.aoc2016.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class Day16Test {

    private val day = 16                                     ///////// Update this for a new dayN test
    private lateinit var solver: DragonChecksum           ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")

    @BeforeEach
    fun setup() {
        solver = DragonChecksum(inputDataReader)
        solver.initialize()
    }

    @Test
    @Order(1)
    fun `Sets Day correctly`() {
        assertThat(solver.day).isEqualTo(day)
    }

    @ParameterizedTest
    @CsvSource(value = [
        "1, 100",
        "0, 001",
        "11111, 11111000000",
        "111100001010, 1111000010100101011110000"
    ])
    @Order(2)
    fun `Calculates Dragon Curve`(a: String, expected: String) {
        assertThat(solver.dragonCurve(a, 2 * a.length + 1)).isEqualTo(expected)
    }

    @Test
    @Order(3)
    fun `Calculates Dragon Checksum`() {
        val data = solver.dragonCurve("10000", 20).also { it.println() }
        val chksum = solver.dragonChecksum(data).also { it.println() }
        assertThat(chksum).isEqualTo("01100")
    }

    @Test
    @Order(4)
    fun `Solves Part 1`() {
        val result = solver.solvePart1().also { it.println() }
        assertThat(result).isEqualTo("01100")
    }

    @Test
    @Order(6)
    fun `Solves Part 2`() {
        println("NA")
        assert(true)
    }
}

