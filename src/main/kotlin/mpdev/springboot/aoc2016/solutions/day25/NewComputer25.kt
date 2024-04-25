package mpdev.springboot.aoc2016.solutions.day25

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import mpdev.springboot.aoc2016.utils.AocException
import mpdev.springboot.aoc2016.utils.Program
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Component

@Component
class NewComputer25(inputDataReader: InputDataReader): PuzzleSolver(inputDataReader, 25) {

    lateinit var program: Program
    val outChannel = Channel<Int>(Channel.UNLIMITED)

    override fun initialize() {
        program = Program(inputData, outChannel)
    }
    suspend fun runProgram(initReg: Map<String,Int> = emptyMap(), expectOutCount: Int = 0): List<Int> {
        val result = mutableListOf<Int>()
        runBlocking {
            val job = launch {  program.run(initReg) }
            job.join()
            while (result.size < expectOutCount)
                result.add(outChannel.receive())
            job.cancel()
        }
        return result
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun runEndlessProgram(initReg: Map<String,Int> = emptyMap()): List<Int> {
        val result = mutableListOf<Int>()
        runBlocking {
            val job = launch {  program.run(initReg, 100) }
            job.join()
            while (!outChannel.isEmpty) {
                result.add(outChannel.receive())
            }
            job.cancel()
        }
        return result
    }

    override fun solvePart1(): Int {
        for (a in 0 .. 10_000) {
            var result: List<Int>
            var solved = false
            runBlocking {
                result = runEndlessProgram(mapOf("a" to a))
                if (result.joinToString("") == StringUtils.repeat("01", 50)) {
                    solved = true
                    return@runBlocking
                }
            }
            if (solved) {
                log.info("program output: {}", result.joinToString(""))
                return a
            }
        }
        throw AocException("no solution found")
    }

    override fun solvePart2(): String = "End of AoC 2016"

}