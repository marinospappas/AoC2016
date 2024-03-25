package mpdev.springboot.aoc2016.day3

import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.day03.TriangleSolver
import mpdev.springboot.aoc2016.solutions.day03.isTriangle
import mpdev.springboot.aoc2016.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day03Test {

    private val day = 3                                     ///////// Update this for a new dayN test
    private lateinit var solver: TriangleSolver             ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")

    @BeforeEach
    fun setup() {
        solver = TriangleSolver(inputDataReader)
    }

    @Test
    @Order(1)
    fun `Sets Day correctly`() {
        assertThat(solver.day).isEqualTo(day)
    }

    @Test
    @Order(2)
    fun `Reads Input`() {
        solver.triangles.forEach { it.println() }
        assertThat(solver.triangles.size).isEqualTo(2)
    }

    @Test
    fun `Identifies Triangles`() {
        assertFalse(solver.triangles[0].isTriangle())
        assertTrue(solver.triangles[1].isTriangle())

    }

    @Test
    @Order(4)
    fun `Solves Part 1`() {
        val result = solver.solvePart1().also { it.println() }
        assertThat(result).isEqualTo(1)
    }

    @Test
    @Order(6)
    fun `Solves Part 2`() {
        inputDataReader.testInput = listOf(
            "101 301 501",
            "102 302 502",
            "103 303 503",
            "201 401 601",
            "202 402 602",
            "203 403 603"
        )
        solver = TriangleSolver(inputDataReader)
        val result = solver.solvePart2().also { it.println() }
        assertThat(result).isEqualTo(6)
    }
}
