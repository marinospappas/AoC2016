package mpdev.springboot.aoc2016.solutions.day12

import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import org.springframework.stereotype.Component

@Component
class NewComputer(inputDataReader: InputDataReader): PuzzleSolver(inputDataReader, 12) {

    lateinit var program: Program

    override fun initialize() {
        program = Program(inputData)
    }

    fun runProgram(initReg: Map<String,Int> = emptyMap()): Int {
        program.run(initReg)
        return program.getRegister("a")
    }

    override fun solvePart1(): Int = runProgram()

    override fun solvePart2(): Int = runProgram(mapOf("c" to 1))

}