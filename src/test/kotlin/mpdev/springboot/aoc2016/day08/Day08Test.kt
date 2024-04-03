package mpdev.springboot.aoc2016.day08

import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.day08.DisplayProcessor
import mpdev.springboot.aoc2016.solutions.day08.Pixel
import mpdev.springboot.aoc2016.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day08Test {

    private val day = 8                                     ///////// Update this for a new dayN test
    private lateinit var solver: DisplayProcessor           ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")

    @BeforeEach
    fun setup() {
        DisplayProcessor.GRID_DIMENSIONS = Pair(7,3)
        solver = DisplayProcessor(inputDataReader)
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
        solver.display.print()
        solver.instructions.forEach { it.println() }
        assertThat(solver.instructions.size).isEqualTo(4)
        assertThat(solver.display.countOf(Pixel.LIT)).isEqualTo(0)
    }

    @Test
    @Order(3)
    fun `Executes Instructions`() {
        solver.display.print()
        solver.instructions.forEach { instr ->
            instr.first.execute(solver, instr.second, instr.third)
            solver.display.print()
            assertThat(solver.display.countOf(Pixel.LIT)).isEqualTo(6)
        }
        assertThat(solver.display.getRow(0).count { it.value == Pixel.LIT }).isEqualTo(3)
        assertThat(solver.display.getRow(1).count { it.value == Pixel.LIT }).isEqualTo(2)
        assertThat(solver.display.getRow(2).count { it.value == Pixel.LIT }).isEqualTo(1)
    }

    @Test
    @Order(4)
    fun `Solves Part 1`() {
        val result = solver.solvePart1().also { it.println() }
        assertThat(result).isEqualTo(6)
    }

    @Test
    @Order(6)
    fun `Solves Part 2`() {
        solver.solvePart1()
        val result = solver.solvePart2().also { it.println() }
        assertThat(result).isEqualTo("")
    }
}
