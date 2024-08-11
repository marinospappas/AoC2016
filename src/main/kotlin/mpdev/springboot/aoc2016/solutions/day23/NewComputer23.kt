package mpdev.springboot.aoc2016.solutions.day23

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import mpdev.springboot.aoc2016.utils.aocvm.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class NewComputer23(inputDataReader: InputDataReader): PuzzleSolver(inputDataReader, 23) {

    lateinit var aocVm: AocVm

    override fun initialize() {
        initialiseOpCodes()
        aocVm = AocVm(inputData.toMutableList().also { it.add(0, "in a") }.also { it.add("out a") })
    }

    override fun solvePart1(): Int {
        var result: Int
        runBlocking {
            aocVm.sendInputToProgram(7)
            val job = launch { aocVm.runProgram() }
            aocVm.waitProgram(job)
            result = aocVm.getOutputFromProgram().last()
        }
        return result
    }

    override fun solvePart2(): Int {
        var result: Int
        log.info("Part 2")
        aocVm = AocVm(inputData.toMutableList().also { it.add(0, "in a") }.also { it.add("out a") })
        runBlocking {
            aocVm.sendInputToProgram(12)
            val job = launch { aocVm.runProgram() }
            aocVm.waitProgram(job)
            result = aocVm.getOutputFromProgram().last()
        }
        return result
    }

    companion object {
        const val TGL = "tgl"
        const val CPY = "cpy"
        fun initialiseOpCodes() {
            InstructionSet.opCodesList["cpy"] = InstructionSet.OpCode("cpy", 2, listOf(ParamReadWrite.R, ParamReadWrite.W)
            ) { a -> Pair(OpResultCode.SET_MEMORY, listOf(a[1], a[0] as Long)) }
            InstructionSet.opCodesList[TGL] = InstructionSet.OpCode(TGL, 1, listOf(ParamReadWrite.R)
            ) { a -> Pair(OpResultCode.CUSTOM, listOf(CustomOpCodeToggle(), a[0] as Long)) }
        }
    }

    class CustomOpCodeToggle: CustomOpCode {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
        override fun execute(instructionList: MutableList< Pair<InstructionSet.OpCode, List<Any>> >, pc: Int, params: List<Any>) {
            val inputValue = (params[1] as Long).toInt()
            if (pc + inputValue < instructionList.size) {
                val curInstr = instructionList[pc + inputValue]
                instructionList[pc + inputValue] =
                    Pair(InstructionSet.getOpCode(toggle(curInstr.first.code)), curInstr.second)
                log.info("TGL: instr $curInstr was changed to ${instructionList[pc + inputValue]}")
            }
        }

        private fun toggle(instr: String) = when(instr) {
            InstructionSet.INC -> InstructionSet.DEC
            InstructionSet.DEC, TGL -> InstructionSet.INC
            InstructionSet.JNZ -> CPY
            CPY -> InstructionSet.JNZ
            InstructionSet.MUL -> InstructionSet.MUL
            InstructionSet.OUT -> InstructionSet.OUT
            else -> InstructionSet.NOP
        }
    }
}