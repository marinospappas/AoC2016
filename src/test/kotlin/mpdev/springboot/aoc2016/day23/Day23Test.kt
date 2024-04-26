package mpdev.springboot.aoc2016.day23

import kotlinx.coroutines.runBlocking
import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.day23.NewComputer23
import mpdev.springboot.aoc2016.utils.Program
import mpdev.springboot.aoc2016.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day23Test {

    private val day = 23                                     ///////// Update this for a new dayN test
    private lateinit var solver: NewComputer23           ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")

    @BeforeEach
    fun setup() {
        solver = NewComputer23(inputDataReader)
        solver.initialize()
    }

    @Test
    @Order(1)
    fun `Sets Day correctly`() {
        assertThat(solver.day).isEqualTo(day)
    }

    @Test
    @Order(2)
    fun `Executes Multiplication 1`() {
        val testCode = listOf(
            "cpy a b",
            "dec b",
            "cpy a d",
            "cpy 0 a",
            "cpy b c",
            "inc a",
            "dec c",
            "jnz c -2",
            "dec d",
            "jnz d -5",
        )
        solver.program = Program(testCode)
        runBlocking {
            solver.runProgram(mapOf("a" to 7))
            println("a = ${solver.program.getRegister("a")}")
            println("b = ${solver.program.getRegister("b")}")
            println("c = ${solver.program.getRegister("c")}")
            println("d = ${solver.program.getRegister("d")}")
        }
    }

    @Test
    @Order(3)
    fun `Executes Multiplication 2`() {
        val testCode = listOf(
            "cpy 89 c",
            "cpy 79 d",
            "inc a",
            "dec d",
            "jnz d -2",
            "dec c",
            "jnz c -5",
        )
        solver.program = Program(testCode)
        runBlocking {
            solver.runProgram(mapOf("a" to 10))
            println("a = ${solver.program.getRegister("a")}")
            println("b = ${solver.program.getRegister("b")}")
            println("c = ${solver.program.getRegister("c")}")
            println("d = ${solver.program.getRegister("d")}")
        }
    }

    @Test
    @Order(4)
    fun `Solves Part 1`() {
        val result = solver.solvePart1().also { it.println() }
        assertThat(result).isEqualTo(3)
    }

    @Test
    @Order(6)
    fun `Solves Part 2`() {
        println("NA")
        assert(true)
    }
}

