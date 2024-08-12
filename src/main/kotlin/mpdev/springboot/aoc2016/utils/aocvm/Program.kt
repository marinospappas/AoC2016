package mpdev.springboot.aoc2016.utils.aocvm

import kotlinx.coroutines.channels.Channel
import mpdev.springboot.aoc2016.utils.AocException
import mpdev.springboot.aoc2016.utils.aocvm.InstructionSet.Companion.getOpCode
import mpdev.springboot.aoc2016.utils.aocvm.OpResultCode.*
import mpdev.springboot.aoc2016.utils.aocvm.ProgramState.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Program(prog: List<String>, private val ioChannel: List<Channel<Long>> = listOf()) {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)
    var programState: ProgramState = READY
    var instanceName = ""

    private val instructionList: MutableList< Pair<InstructionSet.OpCode, List<Any>> > = if (prog[0].equals("#test", true))
        mutableListOf()
    else
        prog.map { it.split(" ") }
            .map { Pair(getOpCode(it[0]), it.subList(1, it.size).map { v -> v.toIntOrString() }) }
            .toMutableList()

    private val registers = mutableMapOf<String,Long>()

    suspend fun run(initReg: Map<String, Long> = emptyMap(), maxCount: Int = Int.MAX_VALUE) {
        var pc = 0
        var outputCount = 0
        registers.clear()
        initReg.forEach { (reg, v) -> registers[reg] = v }
        while (pc <= instructionList.lastIndex && outputCount < maxCount) {
            val (instr, params) = instructionList[pc]
            val mappedParams = mapParams(params, instr.paramMode, instr.numberOfParams)
            log.debug("$instanceName pc: $pc instruction: ${instr.code} $mappedParams")
            val (resCode, values) = instr.execute(mappedParams)
            log.debug("$instanceName     result: $resCode $values")
            when (resCode) {
                SET_MEMORY -> registers[values[0] as String] = valueOf(values[1])
                INCR_PC -> pc += valueOf(values[0]).toInt() - 1
                OUTPUT -> {
                    log.debug("AocProg {} writing to output {}", instanceName, values[0])
                    ioChannel[1].send(valueOf(values[0]))
                    ++outputCount
                }
                INPUT -> {
                    programState = WAIT
                    log.debug("AocProg {} waiting for input will be stored in {}", instanceName, values[0])
                    // will be suspended below and the value in retCode may change
                    val inputValue = ioChannel[0].receive()
                    registers[values[0] as String] = inputValue
                    programState = RUNNING
                    log.debug("AocProg {} received input {} to be stored in {}", instanceName, inputValue, values[0])
                }
                CUSTOM -> (values[0] as CustomOpCode).execute(instructionList, pc, values)
                EXIT -> break
                NONE -> {}
            }
            ++pc
        }
        programState = COMPLETED
    }

    fun getRegister(reg: String): Long = registers.getOrPut(reg) { 0 }
    fun setRegister(reg: String, value: Long) {
        registers[reg] = value
    }

    fun getMemory(address: Int) = getRegister(address.toString())
    fun setMemory(address: Int, value: Long) {
        setRegister(address.toString(), value)
    }

    private fun valueOf(s: Any) =
        when (s) {
            is Int -> s.toLong()
            is Long -> s
            is String -> registers.getOrPut(s) { 0 }
            else -> throw AocException("$instanceName unexpected error PROG001 [$s]")
        }

    private fun String.toIntOrString() = try {
            this.toInt()
        } catch (e: Exception) {
            this
        }

    private fun mapParams(params: List<Any>, paramMode: List<ParamReadWrite>, numberOfParams: Int): List<Any> {
        val newParams = mutableListOf<Any>()
        // if the number of params is > than the number if the input params then the first param is added as last as well (to be used as output)
        val thisParams = if (numberOfParams > params.size) params + params[0] else params
        thisParams.indices.forEach { i ->
            // R params are translated to value - W params are left as they are
            newParams.add(if (paramMode[i] == ParamReadWrite.R) valueOf(thisParams[i]) else thisParams[i])
        }
        return newParams
    }
}

enum class ProgramState {
    READY, RUNNING, WAIT, COMPLETED
}