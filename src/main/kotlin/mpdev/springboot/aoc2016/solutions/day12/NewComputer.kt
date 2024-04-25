package mpdev.springboot.aoc2016.solutions.day12

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import mpdev.springboot.aoc2016.utils.Program
import org.springframework.stereotype.Component

@Component
class NewComputer(inputDataReader: InputDataReader): PuzzleSolver(inputDataReader, 12) {

    lateinit var program: Program

    override fun initialize() {
        program = Program(inputData)
    }

    suspend fun runProgram(initReg: Map<String,Int> = emptyMap()): Int {
        runBlocking {
            val job = launch {  program.run(initReg) }
            job.join()
        }
        return program.getRegister("a")
    }

    override fun solvePart1(): Int {
        val result: Int
        runBlocking {
            result = runProgram()
        }
        return result
    }

    override fun solvePart2(): Int {
        val result: Int
        runBlocking {
            result = runProgram(mapOf("c" to 1))
        }
        return result
    }

}