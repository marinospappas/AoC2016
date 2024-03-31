package mpdev.springboot.aoc2016.day10

import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.day10.MicrochipFactory
import mpdev.springboot.aoc2016.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day10Test {

    private val day = 10                                     ///////// Update this for a new dayN test
    private lateinit var solver: MicrochipFactory            ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")

    @BeforeEach
    fun setup() {
        solver = MicrochipFactory(inputDataReader)
        MicrochipFactory.debug = true
        MicrochipFactory.resultChips = listOf(2,5)
    }

    @Test
    @Order(1)
    fun `Sets Day correctly`() {
        assertThat(solver.day).isEqualTo(day)
    }

    @Test
    @Order(2)
    fun `Reads Input`() {
        solver.robots.forEach { it.println() }
        solver.inputMap.forEach { it.println() }
        assertThat(solver.robots.size).isEqualTo(3)
        assertThat(solver.inputMap.size).isEqualTo(3)
    }

    @Test
    @Order(2)
    fun `Processes Chips`() {
        solver.robots.forEach { it.println() }
        val result = solver.runProcess()
        assertThat(result).isEqualTo(2)
    }

    @Test
    @Order(4)
    fun `Solves Part 1`() {
        val result = solver.solvePart1().also { it.println() }
        assertThat(result).isEqualTo(2)
    }

    @Test
    @Order(6)
    fun `Solves Part 2`() {
        solver.solvePart1()
        val result = solver.solvePart2().also { it.println() }
        assertThat(result).isEqualTo(30)
    }
}

