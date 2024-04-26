package mpdev.springboot.aoc2016.solutions.day23

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import mpdev.springboot.aoc2016.utils.Program
import org.springframework.stereotype.Component

@Component
class NewComputer23(inputDataReader: InputDataReader): PuzzleSolver(inputDataReader, 23) {

    lateinit var program: Program

    override fun initialize() {
        program = Program(inputData)
    }
    suspend fun runProgram(initReg: Map<String,Int> = emptyMap()): Int {
        runBlocking {
            val job = launch { program.run(initReg) }
            job.join()
        }
        return program.getRegister("a")
    }

    override fun solvePart1(): Int {
        var result: Int
        runBlocking {
            result = runProgram(mapOf("a" to 7))
        }
        return result
    }

    override fun solvePart2(): String = "End of AoC 2016"

}