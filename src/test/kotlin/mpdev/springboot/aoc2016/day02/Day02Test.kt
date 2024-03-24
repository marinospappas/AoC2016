package mpdev.springboot.aoc2016.day02

import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.day02.Day02
import mpdev.springboot.aoc2016.solutions.day02.KeypadDecoder
import mpdev.springboot.aoc2016.utils.Point
import mpdev.springboot.aoc2016.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day02Test {

    private val day = 2                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day02()                      ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")
    private var inputLines: List<String> = inputDataReader.read(day)

    @BeforeEach
    fun setup() {
        puzzleSolver.setDay()
        puzzleSolver.inputData = inputLines
        puzzleSolver.initSolver()
    }

    @Test
    @Order(1)
    fun `Sets Day correctly`() {
        assertThat(puzzleSolver.day).isEqualTo(day)
    }

    @Test
    @Order(2)
    fun `Reads Input and sets the Instructions List`() {
        val keypadDecoder = KeypadDecoder(inputLines)
        KeypadDecoder.keypad1.print()
        val start = KeypadDecoder.getStartingPosition(KeypadDecoder.keypad1).also {it.println()}
        keypadDecoder.instructions.forEach { it.println() }
        assertThat(start).isEqualTo(Point(1,1))
        assertThat(keypadDecoder.instructions.size).isEqualTo(4)
    }

    @Test
    @Order(3)
    fun `Follows Instructions`() {
        val keypadDecoder = KeypadDecoder(inputLines)
        val result = keypadDecoder.findCode(KeypadDecoder.keypad1).also { it.println() }
        assertThat(result).isEqualTo("1985")
    }

    @Test
    @Order(4)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("1985")
    }

    @Test
    @Order(5)
    fun `Follows Instructions for keypad 2`() {
        val keypadDecoder = KeypadDecoder(inputLines)
        KeypadDecoder.keypad2.print()
        val start = KeypadDecoder.getStartingPosition(KeypadDecoder.keypad2).also {it.println()}
        val result = keypadDecoder.findCode(KeypadDecoder.keypad2).also { it.println() }
        assertThat(start).isEqualTo(Point(0,2))
        assertThat(result).isEqualTo("5DB3")
    }

    @Test
    @Order(6)
    fun `Solves Part 2`() {
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("5DB3")
    }
}
