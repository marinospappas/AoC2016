package mpdev.springboot.aoc2016.solutions.day02

import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import mpdev.springboot.aoc2016.utils.Grid
import mpdev.springboot.aoc2016.utils.GridUtils
import mpdev.springboot.aoc2016.utils.Point
import org.springframework.stereotype.Component

@Component
class KeypadDecoder(inputDataReader: InputDataReader): PuzzleSolver(inputDataReader, 2) {

    lateinit var instructions: List<List<GridUtils.Direction>>

    override fun initialize() {
        instructions = inputData.map { line -> line.toList().map { c -> GridUtils.Direction.of(c) } }
    }

    private fun nextKey(curKey: Point, instrList: List<GridUtils.Direction>, keypad: Grid<Char>): Point {
        var curPosition = curKey
        for (instr in instrList) {
            val newPosition = curPosition.plus(instr.increment)
            keypad.getDataPoint(curPosition.plus(instr.increment)) ?: continue
            curPosition =newPosition
        }
        return curPosition
    }

    fun Grid<Char>.findCode(): String {
        var curKey = getStartingPosition(this)
        return instructions.map { instr ->
            getDataPoint(nextKey(curKey, instr, this).also { curKey = it })
        }.joinToString("")
    }

    override fun solvePart1(): String = keypad1.findCode()

    override fun solvePart2(): String = keypad2.findCode()

    companion object {
        private const val START_KEY = '5'
        val keypad1 = Grid(listOf("123", "456", "789"), ('0'..'9').associateWith { it }, border = 0)
        private val keypad2List = listOf(
            "  1  ",
            " 234 ",
            "56789",
            " ABC ",
            "  D  "
        )
        private val keypad2GridMap = keypad2List.indices.map { y -> keypad2List[y].toList()
            .associateBy { Point(keypad2List[y].indexOf(it), y) }
            .filterNot { e -> e.value == ' ' }
        }.flatMap { map -> map.entries }.associate { Pair(it.key, it.value) }
        val keypad2 = Grid(keypad2GridMap, (('0'..'9').toSet() + ('A'..'D').toSet()).associateWith { it }, border = 0)

        fun getStartingPosition(keypad: Grid<Char>): Point =
            keypad.getDataPoints().filter { e -> e.value == START_KEY }.map { e -> e.key }.first()

    }
}

