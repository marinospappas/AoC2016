package mpdev.springboot.aoc2016.solutions.day25

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import mpdev.springboot.aoc2016.solutions.day12.NewComputer
import mpdev.springboot.aoc2016.utils.AocException
import mpdev.springboot.aoc2016.utils.aocvm.AbstractAocVm
import mpdev.springboot.aoc2016.utils.aocvm.AocVm
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Component

@Component
class NewComputer25(inputDataReader: InputDataReader): PuzzleSolver(inputDataReader, 25) {

    lateinit var aocVm: AocVm


    override fun initialize() {
        NewComputer.initialiseOpCodes()
        aocVm = AocVm(inputData.toMutableList().also { it.add(0, "in a") }.also { it.add("out a") })
        aocVm.aocCtl(AbstractAocVm.AocCmd.SET_OUTPUT_BUFFER_SIZE, 10)
    }

    override fun solvePart1(): Int {
        for (a in 0 .. 10_000) {
            val result = mutableListOf<Int>()
            var solved = false
            runBlocking {
                aocVm.sendInputToProgram(a)
                val job = launch { aocVm.runProgram() }
                while (result.size < 50) {
                    result.addAll(aocVm.getAsyncOutputFromProgram())
                }
                job.cancel()
                if (result.joinToString("").startsWith(StringUtils.repeat("01", 25))) {
                    solved = true
                    return@runBlocking
                }
            }
            if (solved) {
                log.info("program output: {}...", result.joinToString(""))
                return a
            }
        }
        throw AocException("2016 day 25 - no solution found")
    }

    override fun solvePart2(): String = "End of AoC 2016"

}