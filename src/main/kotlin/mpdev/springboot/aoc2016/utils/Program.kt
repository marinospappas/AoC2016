package mpdev.springboot.aoc2016.utils

import kotlinx.coroutines.channels.Channel
import mpdev.springboot.aoc2016.utils.Instruction.*

class Program(prog: List<String>, private val outputChannel: Channel<Int> = Channel()) {

    val instructionList: List< Triple<Instruction, Any, Any> >  = if (prog[0].equals("#test", true))
        emptyList()
    else
        prog.map { it.split(" ") }.map { it + "" }
            .map { Triple(Instruction.fromString(it[0]), it[1].toIntOrString(), it[2].toIntOrString()) }
    private val registers = mutableMapOf<String,Int>()

    suspend fun run(initReg: Map<String,Int> = emptyMap(), maxCount: Int = Int.MAX_VALUE) {
        var pc = 0
        var outputCount = 0
        registers.clear()
        initReg.forEach { (reg, v) -> registers[reg] = v }
        while (pc <= instructionList.lastIndex) {
            val (instr, reg, param) = instructionList[pc]
            when (instr) {
                CPY -> registers[param.toString()] = valueOf(reg)  // reg and param are reversed in cpy instruction
                INC -> registers[reg.toString()] = registers.getOrPut(reg.toString()) { 0 } + 1
                DEC -> registers[reg.toString()] = registers.getOrPut(reg.toString()) { 0 } - 1
                JNZ -> if (valueOf(reg) != 0) pc += valueOf(param) - 1
                OUT -> { outputChannel.send(valueOf(reg)); ++outputCount }
            }
            ++pc
            if (outputCount >= maxCount)
                return
        }
    }

    fun getRegister(reg: String): Int = registers.getOrPut(reg) { 0 }

    private fun valueOf(s: Any) =
        if (s is Int)
            s
        else
        if (s is String)
            registers.getOrPut(s) { 0 }
        else
            throw AocException("unexpected error 021")

    private fun String.toIntOrString() =
        try {
            this.toInt()
        }
        catch (e: Exception) { this }
}

enum class Instruction(val value: String) {
    CPY("cpy"),
    INC("inc"),
    DEC("dec"),
    JNZ("jnz"),
    OUT("out");
    companion object {
        fun fromString(str: String): Instruction {
            return Instruction.values().firstOrNull { it.value == str } ?: throw AocException("no operation found for [$str]")
        }
    }
}
