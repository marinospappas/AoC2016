package mpdev.springboot.aoc2016.solutions.day12

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import mpdev.springboot.aoc2016.utils.aocvm.AocVm
import mpdev.springboot.aoc2016.utils.aocvm.InstructionSet
import mpdev.springboot.aoc2016.utils.aocvm.OpResultCode
import mpdev.springboot.aoc2016.utils.aocvm.ParamReadWrite
import org.springframework.stereotype.Component

@Component
class NewComputer(inputDataReader: InputDataReader): PuzzleSolver(inputDataReader, 12) {

    lateinit var aocVm: AocVm

    override fun initialize() {
        initialiseOpCodes()
        aocVm = AocVm(inputData.toMutableList().also { it.add(0, "in c") }.also { it.add("out a") })
    }

    override fun solvePart1(): Int {
        var result: Int
        runBlocking {
            aocVm.sendInputToProgram(0)
            val job = launch { aocVm.runProgram() }
            aocVm.waitProgram(job)
            result = aocVm.getOutputFromProgram().last()
        }
        return result
    }

    override fun solvePart2(): Int {
        val result: Int
        runBlocking {
            aocVm.sendInputToProgram(1)
            val job = launch { aocVm.runProgram() }
            result = aocVm.getOutputFromProgram().last()
            aocVm.waitProgram(job)
        }
        return result
    }

    companion object {
        fun initialiseOpCodes() {
            InstructionSet.opCodesList["cpy"] = InstructionSet.OpCode("cpy", 2,
                listOf(ParamReadWrite.R, ParamReadWrite.W)
            ) { a -> Pair(OpResultCode.SET_MEMORY, listOf(a[1], a[0] as Long)) }
        }
    }
}