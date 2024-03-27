package mpdev.springboot.aoc2016.solutions.day06

import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import mpdev.springboot.aoc2016.utils.toFrequency
import org.springframework.stereotype.Component

@Component
class SignalCorrection(inputDataReader: InputDataReader): PuzzleSolver(inputDataReader, 6) {

    override fun solvePart1(): String =
        (0..(inputData[0].lastIndex))
            .map { indx -> inputData.map { line -> line[indx] }.joinToString("").toFrequency().first() }
            .joinToString("")

    override fun solvePart2():  String =
        (0..(inputData[0].lastIndex))
            .map { indx -> inputData.map { line -> line[indx] }.joinToString("").toFrequency().last() }
            .joinToString("")

}
