package mpdev.springboot.aoc2016.solutions.day16

import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import org.springframework.stereotype.Component
import kotlin.math.min

@Component
class DragonChecksum(inputDataReader: InputDataReader): PuzzleSolver(inputDataReader, 16) {

    fun dragonCurve(input: String, length: Int): String {
        val a = StringBuilder(input)
        do {
            val b = a.reversed().toString().replace('0','x').replace('1','0').replace('x','1')
            a.append('0').append(b)
        } while (a.length < length)
        return a.substring(0, length)
    }

    fun checkSum(a: String): String {
        val sb = StringBuilder()
        for (i in 0 .. a.lastIndex-1 step(2))
            sb.append(if (a[i] == a[i+1]) '1' else '0')
        return sb.toString()
    }

    fun dragonChecksum(a: String): String {
        var chcksm = a
        do {
            chcksm = checkSum(chcksm)
        } while (chcksm.length % 2 == 0)
        return chcksm
    }

    override fun solvePart1(): String {
        val data = dragonCurve(inputData[0], inputData[1].toInt())
        log.info("data part 1: {}...", data.substring(0, min(data.length, 100)))
        return dragonChecksum(data)
    }

    override fun solvePart2(): String {
        val data = dragonCurve(inputData[0], 35_651_584)
        log.info("data part 2: {}...", data.substring(0, 100))
        return dragonChecksum(data)
    }
}
