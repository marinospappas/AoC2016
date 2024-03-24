package mpdev.springboot.aoc2016.solutions.day02

import mpdev.springboot.aoc2016.utils.Grid
import mpdev.springboot.aoc2016.utils.GridUtils
import mpdev.springboot.aoc2016.utils.Point

class KeypadDecoder(input: List<String>) {

    val instructions: List<List<GridUtils.Direction>> = input
        .map { line -> line.toList().map { c -> GridUtils.Direction.of(c) } }

    private fun nextKey(curKey: Point, instrList: List<GridUtils.Direction>, keypad: Grid<Char>): Point {
        var curPosition = curKey
        for (instr in instrList) {
            val newPosition = curPosition.plus(instr.increment)
            keypad.getDataPoint(curPosition.plus(instr.increment)) ?: continue
            curPosition =newPosition
        }
        return curPosition
    }

    fun findCode(keypad: Grid<Char>): String {
        var curKey = getStartingPosition(keypad)
        return instructions.map { instr ->
            keypad.getDataPoint(nextKey(curKey, instr, keypad).also { curKey = it })
        }.joinToString("")
    }

    companion object {
        val keypad1 = Grid(listOf("123", "456", "789"), ('0'..'9').associateWith { it }, border = 0)
        val keypad2List = listOf(
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
        val keypad2 = Grid(keypad2GridMap, (setOf('1','2','3','4','5','6','7','8','9') + setOf('A','B','C','D')).associateWith { it }, border = 0)

        fun getStartingPosition(keypad: Grid<Char>): Point =
            keypad.getDataPoints().filter { e -> e.value == '5' }.map { e -> e.key }.first()

    }
}

