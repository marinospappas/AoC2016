package mpdev.springboot.aoc2016.day18

import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.day18.SafePathFinder
import mpdev.springboot.aoc2016.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day18Test {

    private val day = 18                                     ///////// Update this for a new dayN test
    private lateinit var solver: SafePathFinder           ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")

    @BeforeEach
    fun setup() {
        SafePathFinder.GRID_SIZE = 10
        solver = SafePathFinder(inputDataReader)
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
        solver.grid.print()
        assertThat(solver.grid.getDataPoints().size).isEqualTo(100)
    }

    @Test
    @Order(4)
    fun `Solves Part 1`() {
        val result = solver.solvePart1().also { it.println() }
        assertThat(result).isEqualTo(38)
    }

    @Test
    @Order(6)
    fun `Solves Part 2`() {
        println("NA")
        assert(true)
    }
}

