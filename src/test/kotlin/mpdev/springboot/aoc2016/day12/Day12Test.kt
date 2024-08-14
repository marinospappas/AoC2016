package mpdev.springboot.aoc2016.day12

import kotlinx.coroutines.runBlocking
import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.day12.NewComputer
import mpdev.springboot.aoc2016.utils.aocvm.AocVm
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
        /*solver.program.instructionList.forEach { it.println() }*/
        /*assertThat(solver.program.instructionList.size).isEqualTo(6)*/
    }

    @Test
    @Order(4)
    fun `Solves Part 1`() {
        val result = solver.solvePart1().also { it.println() }
        assertThat(result).isEqualTo(42)
    }

    @ParameterizedTest
    @CsvSource(value = ["0, 26", "1, 33"])
    @Order(5)
    fun `Executes Program Part 1`(c: Long, expected: Int) {
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
        val aocTest = AocVm(testCode)
        runBlocking {
            // sets d to 26 or 33 depending on the initial value of c
            aocTest.runProgram(mapOf("c" to c))
            println("a = ${aocTest.getProgramRegister("a")}")
            println("b = ${aocTest.getProgramRegister("b")}")
            println("c = ${aocTest.getProgramRegister("c")}")
            println("d = ${aocTest.getProgramRegister("d")}")
        }
        assertThat(aocTest.getProgramRegister("d")).isEqualTo(expected)
    }

    @ParameterizedTest
    @CsvSource(value = ["26", "33"])
    @Order(6)
    fun `Executes Program Part 2`(d: Long) {
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
        val testCode1 = listOf(
            "cpy a c",
            "add a b",
            "out a",
            "cpy c b",
            "dec d",
            "jnz d -5"
        )
        val aocTest = AocVm(testCode)
        aocTest.newProgram(testCode1)
        val result = mutableListOf<Long>()
        val result1 = mutableListOf<Long>()
        runBlocking {
            // calculates the 28th (26+2) or 35th (33+2) Fibonacci number
            aocTest.runProgram(mapOf("a" to 1, "b" to 1, "c" to 0, "d" to d))
            result.addAll(aocTest.getAsyncOutputFromProgramLong())
            println("a = ${result[0]}")
            println("values of a: $result")
            println("a = ${aocTest.getProgramRegisterLong("a")}")
            println("b = ${aocTest.getProgramRegisterLong("b")}")
            println("c = ${aocTest.getProgramRegisterLong("c")}")
            println("d = ${aocTest.getProgramRegisterLong("d")}")

            aocTest.runProgram(mapOf("a" to 1, "b" to 1, "c" to 0, "d" to d), programId = 1)
            result1.addAll(aocTest.getAsyncOutputFromProgramLong(1))
            println("a = ${result1[0]}")
            println("values of a: $result1")
            // the final part of the code adds 196 to it
        }
        assertThat(result1).isEqualTo(result)
    }

    @Test
    @Order(7)
    fun `Solves Part 2`() {
        val result = solver.solvePart2().also { it.println() }
        assertThat(result).isEqualTo(42)
    }
}

