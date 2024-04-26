package mpdev.springboot.aoc2016.utils

import kotlinx.coroutines.channels.Channel
import mpdev.springboot.aoc2016.utils.Instruction.*
import mpdev.springboot.aoc2016.utils.Instruction.Companion.toggle

class Program(prog: List<String>, private val outputChannel: Channel<Int> = Channel()) {

    val instructionList: MutableList< Triple<Instruction, Any, Any> >  = if (prog[0].equals("#test", true))
        mutableListOf()
    else
        prog.map { it.split(" ") }.map { it + "" }
            .map { Triple(Instruction.fromString(it[0]), it[1].toIntOrString(), it[2].toIntOrString()) }
            .toMutableList()
    private val registers = mutableMapOf<String,Int>()

    suspend fun run(initReg: Map<String,Int> = emptyMap(), maxCount: Int = Int.MAX_VALUE) {
        var pc = 0
        var outputCount = 0
        registers.clear()
        initReg.forEach { (reg, v) -> registers[reg] = v }
        while (pc <= instructionList.lastIndex && outputCount < maxCount) {
            val (instr, reg, param) = instructionList[pc]
            when (instr) {
                CPY -> if (param is String) registers[param] = valueOf(reg)  // reg and param are reversed in cpy instruction
                INC -> registers[reg.toString()] = registers.getOrPut(reg.toString()) { 0 } + 1
                DEC -> registers[reg.toString()] = registers.getOrPut(reg.toString()) { 0 } - 1
                MUL -> {}  // TODO
                JNZ -> if (valueOf(reg) != 0) pc += valueOf(param) - 1
                TGL -> if (pc + valueOf(reg) < instructionList.size) {
                    val curInstr = instructionList[pc + valueOf(reg)]
                    instructionList[pc + valueOf(reg)] = Triple(toggle(curInstr.first), curInstr.second, curInstr.third)
                }
                OUT -> { outputChannel.send(valueOf(reg)); ++outputCount }
                NOP -> {}
            }
            ++pc
        }
    }

    fun getRegister(reg: String): Int = registers.getOrPut(reg) { 0 }

    private fun valueOf(s: Any) =
        when (s) {
            is Int -> s
            is String -> registers.getOrPut(s) { 0 }
            else -> throw AocException("unexpected error PROG001 [$s]")
        }

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
    MUL("mul"),
    JNZ("jnz"),
    TGL("tgl"),
    OUT("out"),
    NOP("nop");
    companion object {
        fun toggle(instr: Instruction) = when(instr) {
            INC -> DEC
            DEC, TGL, OUT -> INC
            JNZ -> CPY
            CPY -> JNZ
            MUL -> MUL
            NOP -> NOP
        }
        fun fromString(str: String): Instruction {
            return Instruction.values().firstOrNull { it.value == str } ?: throw AocException("no operation found for [$str]")
        }
    }
}
