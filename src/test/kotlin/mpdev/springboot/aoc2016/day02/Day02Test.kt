package mpdev.springboot.aoc2016.day02

import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.day02.KeypadDecoder
import mpdev.springboot.aoc2016.utils.Point
import mpdev.springboot.aoc2016.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day02Test {

    private val day = 2                                     ///////// Update this for a new dayN test
    private lateinit var solver: KeypadDecoder              ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")

    @BeforeEach
    fun setup() {
        solver = KeypadDecoder(inputDataReader)
    }

    @Test
    @Order(1)
    fun `Sets Day correctly`() {
        assertThat(solver.day).isEqualTo(day)
    }

    @Test
    @Order(2)
    fun `Reads Input and sets the Instructions List`() {
        KeypadDecoder.keypad1.print()
        val start = KeypadDecoder.getStartingPosition(KeypadDecoder.keypad1).also {it.println()}
        solver.instructions.forEach { it.println() }
        assertThat(start).isEqualTo(Point(1,1))
        assertThat(solver.instructions.size).isEqualTo(4)
    }

    @Test
    @Order(4)
    fun `Solves Part 1`() {
        val result = solver.solvePart1().also { it.println() }
        assertThat(result).isEqualTo("1985")
    }

    @Test
    @Order(6)
    fun `Solves Part 2`() {
        KeypadDecoder.keypad2.print()
        val start = KeypadDecoder.getStartingPosition(KeypadDecoder.keypad2).also {it.println()}
        val result = solver.solvePart2().also { it.println() }
        assertThat(start).isEqualTo(Point(0,2))
        assertThat(result).isEqualTo("5DB3")
    }
}
