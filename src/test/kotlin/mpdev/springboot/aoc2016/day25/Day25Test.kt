package mpdev.springboot.aoc2016.day25

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.day25.NewComputer25
import mpdev.springboot.aoc2016.utils.aocvm.AbstractAocVm
import mpdev.springboot.aoc2016.utils.aocvm.AocVm
import mpdev.springboot.aoc2016.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.io.File

class Day25Test {

    private val day = 25                                     ///////// Update this for a new dayN test
    private lateinit var solver: NewComputer25           ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")

    @BeforeEach
    fun setup() {
        solver = NewComputer25(inputDataReader)
        solver.initialize()
    }

    @Test
    @Order(1)
    fun `Sets Day correctly`() {
        assertThat(solver.day).isEqualTo(day)
    }

    @ParameterizedTest
    @CsvSource(value = [
        "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13",
    ])
    @Order(2)
    fun `Calculates Values for Part 1 of the Program`(a: Int) {
        val testCode = listOf(
            "in a",
            "cpy a d",
            "cpy 4 c",
            "cpy 633 b",
            "inc d",
            "dec b",
            "jnz b -2",
            "dec c",
            "jnz c -5",
            "cpy d a",
            "out a"
        )
        val aocVm = AocVm(testCode)
        val result = mutableListOf<Int>()
        runBlocking {
            aocVm.sendInputToProgram(a)
            aocVm.aocCtl(AbstractAocVm.AocCmd.SET_OUTPUT_BUFFER_SIZE, 1)
            val job = launch { aocVm.runProgram() }
            while (result.isEmpty())
                result.addAll(aocVm.getAsyncOutputFromProgram())
            job.cancel()
            println("a = ${result[0]}")
        }
    }

    @ParameterizedTest
    @CsvSource(value = [
        "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13",
    ])
    @Order(3)
    fun `Calculates Values for Part 2`(a: Int) {
        val testCode = listOf(
            "in a",
            "jnz 0 0",
            "cpy a b",
            "cpy 0 a",
            "cpy 2 c",
            "jnz b 2",
            "jnz 1 6",
            "dec b",
            "dec c",
            "jnz c -4",
            "inc a",
            "jnz 1 -7",
            "out a",
            "out b",
            "out c",
        )
        val testCode1 = listOf(
            "in a",
            "cpy a c",
            "div a 2",
            "mod c 2",
            "jnz c 2",
            "add c 2",
            "out a",
            "out b",
            "out c",
        )
        val aocVm = AocVm(testCode)
        aocVm.aocCtl(AbstractAocVm.AocCmd.SET_OUTPUT_BUFFER_SIZE, 3)
        aocVm.newProgram(testCode1)
        aocVm.aocCtl(AbstractAocVm.AocCmd.SET_OUTPUT_BUFFER_SIZE, 3, 1)
        val result = mutableListOf<Int>()
        val result1 = mutableListOf<Int>()
        runBlocking {
            aocVm.sendInputToProgram(a + 2532)
            val job = launch { aocVm.runProgram() }
            while (result.size < 3)
                result.addAll(aocVm.getAsyncOutputFromProgram())
            job.cancel()
            println("(a,b,c) = $result")

            aocVm.sendInputToProgram(a + 2532, 1)
            val job1 = launch { aocVm.runProgram(programId = 1) }
            while (result1.size < 3)
                result1.addAll(aocVm.getAsyncOutputFromProgram(1))
            job1.cancel()
            println("(a,b,c) = $result1")
        }
        assertThat(result1).isEqualTo(result)
    }

    @ParameterizedTest
    @CsvSource(value = [
        "0, 2", "1, 1", "2, 2", "3, 1", "4, 2", "5, 1", "6, 2", "7, 1", "8, 2", "9, 1", "10, 2", "11, 1", "12, 2", "13, 1", "198, 2"
    ])
    @Order(4)
    fun `Calculates Values for Part 3`(a: Int, c: Int) {
        val testCode = listOf(
            "in a",
            "in c",
            "cpy 2 b",
            "jnz c 2",
            "jnz 1 4",
            "dec b",
            "dec c",
            "jnz 1 -4",
            "jnz 0 0",
            "out a",
            "out b",
            "out c",
        )
        val aocVm = AocVm(testCode)
        val result = mutableListOf<Int>()
        runBlocking {
            aocVm.sendInputToProgram(listOf((a + 2532) / 2, c))
            aocVm.aocCtl(AbstractAocVm.AocCmd.SET_OUTPUT_BUFFER_SIZE, 3)
            val job = launch { aocVm.runProgram() }
            while (result.size < 3)
                result.addAll(aocVm.getAsyncOutputFromProgram())
            job.cancel()
            println("(a,b,c) = $result")
        }
    }

    @ParameterizedTest
    @CsvSource(value = [
        "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13",
        "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "198",
    ])
    @Order(4)
    fun `Solves Part 1`(a: Long) {
        solver.aocVm = AocVm(File("src/main/resources/inputdata/input25_1.txt").readLines())
        solver.aocVm.aocCtl(AbstractAocVm.AocCmd.SET_OUTPUT_BUFFER_SIZE, 10)
        val result = mutableListOf<Int>()
        runBlocking {
            val job = launch { solver.aocVm.runProgram(mapOf("a" to a)) }
            while (result.size < 50)
                result.addAll(solver.aocVm.getAsyncOutputFromProgram())
            job.cancel()
            result.joinToString("").println()
        }
    }

    @Test
    @Order(6)
    fun `Solves Part 2`() {
        println("NA")
        assert(true)
    }
}

