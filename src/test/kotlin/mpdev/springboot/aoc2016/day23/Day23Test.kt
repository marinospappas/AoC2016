package mpdev.springboot.aoc2016.day23

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.day23.NewComputer23
import mpdev.springboot.aoc2016.utils.aocvm.AocVm
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
            "in a",
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
            "out a"
        )
        val aocVm = AocVm(testCode)
        val result: Int
        runBlocking {
            aocVm.sendInputToProgram(7)
            val job = launch { aocVm.runProgram() }
            aocVm.waitProgram(job)
            result = aocVm.getFinalOutputFromProgram().last()
        }
        println("a = $result")
        assertThat(result).isEqualTo(42)
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

