package mpdev.springboot.aoc2016.day17

import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.day17.SecureVault
import mpdev.springboot.aoc2016.utils.combinations
import mpdev.springboot.aoc2016.utils.permutations
import mpdev.springboot.aoc2016.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class Day17Test {

    private val day = 17                                     ///////// Update this for a new dayN test
    private lateinit var solver: SecureVault           ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")

    @BeforeEach
    fun setup() {
        solver = SecureVault(inputDataReader)
        solver.initialize()
    }

    @Test
    @Order(1)
    fun `Sets Day correctly`() {
        assertThat(solver.day).isEqualTo(day)
    }

    @Test
    @Order(2)
    fun `Sets Grid`() {
        solver.grid.print()
        assertThat(solver.grid.getDataPoints().size).isEqualTo(81)
    }

    @Test
    @Order(4)
    fun `Solves Part 1`() {
        val expected = listOf(
            "DDRRRD",
            "DDUDRLRRUDRD",
            "DRURDRUDDLLDLUURRDULRLDUUDDDRR"
        )
        for (i in solver.inputData.indices) {
            solver.inputCode = solver.inputData[i]
            val result = solver.solvePart1().also { it.println() }
            assertThat(result).isEqualTo(expected[i])
        }
    }

    @Test
    @Order(6)
    fun `Solves Part 2`() {
        val expected = listOf(370, 492, 830)
        for (i in solver.inputData.indices) {
            solver.inputCode = solver.inputData[i]
            val result = solver.solvePart2().also { it.println() }
            assertThat(result).isEqualTo(expected[i])
        }
    }
}

