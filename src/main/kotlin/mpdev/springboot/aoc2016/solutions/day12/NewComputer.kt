package mpdev.springboot.aoc2016.solutions.day12

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import mpdev.springboot.aoc2016.utils.Program
import org.springframework.stereotype.Component

@Component
class NewComputer(inputDataReader: InputDataReader): PuzzleSolver(inputDataReader, 12) {

    lateinit var program: Program
    val outChannel = Channel<Int>(Channel.UNLIMITED)

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

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun runProgramWitOutput(initReg: Map<String,Int> = emptyMap()): List<Int> {
        val result = mutableListOf<Int>()
        runBlocking {
            val job = launch {  program.run(initReg) }
            job.join()
            while (!outChannel.isEmpty) {
                result.add(outChannel.receive())
            }
            job.cancel()
        }
        return result
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