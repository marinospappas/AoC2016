package mpdev.springboot.aoc2016.utils.aocvm

interface CustomOpCode {
    fun execute(instructionList: MutableList< Pair<InstructionSet.OpCode, List<Any>> >, pc: Int, params: List<Any>)
}