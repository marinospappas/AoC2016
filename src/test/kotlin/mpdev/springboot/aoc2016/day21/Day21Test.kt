package mpdev.springboot.aoc2016.day21

import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.day21.ScrambledLetters
import mpdev.springboot.aoc2016.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day21Test {

    private val day = 21                                     ///////// Update this for a new dayN test
    private lateinit var solver: ScrambledLetters           ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")

    @BeforeEach
    fun setup() {
        solver = ScrambledLetters(inputDataReader)
        solver.initialize()
        solver.inputString = "abcde"
    }

    @Test
    @Order(1)
    fun `Sets Day correctly`() {
        assertThat(solver.day).isEqualTo(day)
    }

    @Test
    @Order(2)
    fun `Reads Input`() {
        solver.instructionList.forEach { it.println() }
        assertThat(solver.instructionList.size).isEqualTo(8)
    }

    @Test
    @Order(4)
    fun `Solves Part 1`() {
        val result = solver.solvePart1().also { it.println() }
        assertThat(result).isEqualTo("decab")
    }

    @Test
    @Order(6)
    fun `Solves Part 2`() {
        println("NA")
        assert(true)
    }
}

