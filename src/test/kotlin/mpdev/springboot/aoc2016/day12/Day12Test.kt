package mpdev.springboot.aoc2016.day12

import kotlinx.coroutines.runBlocking
import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.day12.NewComputer
import mpdev.springboot.aoc2016.utils.Program
import mpdev.springboot.aoc2016.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class Day12Test {

    private val day = 12                                     ///////// Update this for a new dayN test
    private lateinit var solver: NewComputer                 ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")

    @BeforeEach
    fun setup() {
        solver = NewComputer(inputDataReader)
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
        solver.program.instructionList.forEach { it.println() }
        assertThat(solver.program.instructionList.size).isEqualTo(6)
    }

    @Test
    @Order(4)
    fun `Solves Part 1`() {
        val result = solver.solvePart1().also { it.println() }
        assertThat(result).isEqualTo(42)
    }

    @ParameterizedTest
    @CsvSource(value = ["0", "1"])
    @Order(5)
    fun `Executes Program Part 1`(c: Int) {
        val testCode = listOf(
            "cpy 1 a",
            "cpy 1 b",
            "cpy 26 d",
            "jnz c 2",
            "jnz 1 5",
            "cpy 7 c",
            "inc d",
            "dec c",
            "jnz c -2"
        )
        solver.program = Program(testCode)
        runBlocking {
            // sets a to 26 or 33 depending on the initial value of c
            solver.runProgram(mapOf("c" to c))
            println("a = ${solver.program.getRegister("a")}")
            println("b = ${solver.program.getRegister("b")}")
            println("c = ${solver.program.getRegister("c")}")
            println("d = ${solver.program.getRegister("d")}")
        }
    }

    @ParameterizedTest
    @CsvSource(value = ["26", "33"])
    @Order(6)
    fun `Executes Program Part 2`(d: Int) {
        val testCode = listOf(
            "cpy a c",
            "inc a",
            "dec b",
            "jnz b -2",
            "out a",
            "cpy c b",
            "dec d",
            "jnz d -7",
        )
        solver.program = Program(testCode, solver.outChannel)
        runBlocking {
            // calculates the 28th (26+2) or 35th (33+2) Fibonacci number
            val result = solver.runProgramWitOutput(mapOf("a" to 1, "b" to 1, "c" to 0, "d" to d))
            println("values of a: $result")
            println("a = ${solver.program.getRegister("a")}")
            println("b = ${solver.program.getRegister("b")}")
            println("c = ${solver.program.getRegister("c")}")
            println("d = ${solver.program.getRegister("d")}")
            // the final part of the code adds 196 to it
        }
    }

    @Test
    @Order(7)
    fun `Solves Part 2`() {
        val result = solver.solvePart2().also { it.println() }
        assertThat(result).isEqualTo(42)
    }
}

