package mpdev.springboot.aoc2016.solutions.day08

import kotlinx.serialization.Serializable
import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import mpdev.springboot.aoc2016.utils.*
import org.springframework.stereotype.Component

@Component
class DisplayProcessor(inputDataReader: InputDataReader): PuzzleSolver(inputDataReader, 8) {

    lateinit var instructions: List<Triple<DisplayCommand, Int, Int>>
    lateinit var display: Grid<Pixel>

    override fun initialize() {
        val aocInputList: List<AoCInput> = InputUtils(AoCInput::class.java).readAoCInput(inputData)
        instructions = aocInputList.map { line -> Triple(DisplayCommand.of(line.cmd), line.param1, line.param2) }
        display = Grid(emptyList(), Pixel.mapper, border = 0, defaultSize = GRID_DIMENSIONS, defaultChar = ' ')
        display.updateDimensions()
    }

    private fun drawRectangle(w: Int, h: Int) {
        for (x in 0 until w)
            for (y in 0 until h)
                display.setDataPoint(Point(x,y), Pixel.LIT)
    }

    private fun rotateColumn(x: Int, n: Int) {
        val col = display.getColumn(x)
        col.forEach { e -> display.removeDataPoint(e.key) }
        col.map { e -> Pair(Point(x, (e.key.y + n) % GRID_DIMENSIONS.second), e.value) }
            .forEach { display.setDataPoint(it.first, it.second) }
    }

    private fun rotateRow(y: Int, n: Int) {
        val row = display.getRow(y)
        row.forEach { e -> display.removeDataPoint(e.key) }
        row.map { e -> Pair(Point((e.key.x + n) % GRID_DIMENSIONS.first, y), e.value) }
            .forEach { display.setDataPoint(it.first, it.second) }
    }

    override fun solvePart1(): Int {
        instructions.forEach { instr -> instr.first.execute(this, instr.second, instr.third) }
        return display.countOf(Pixel.LIT)
    }

    override fun solvePart2(): String {
        display.print()
        return ""
    }

    companion object {
        var GRID_DIMENSIONS = Pair(50, 6)
        fun rect(displayProcessor: DisplayProcessor, a: Int, b: Int) { displayProcessor.drawRectangle(a, b) }
        fun rotCol(displayProcessor: DisplayProcessor, x: Int, n: Int) { displayProcessor.rotateColumn(x, n) }
        fun rotRow(displayProcessor: DisplayProcessor, y: Int, n: Int) { displayProcessor.rotateRow(y, n) }
    }
}

enum class DisplayCommand(val value: String, val execute: (DisplayProcessor, Int, Int) -> Unit) {
    DrawRectangle("rect", { d, a, b -> DisplayProcessor.rect(d, a, b) }),
    RotateColumn("rotate_column", { d, a, b -> DisplayProcessor.rotCol(d, a, b) } ),
    RotateRow("rotate_row", { d, a, b -> DisplayProcessor.rotRow(d, a, b) });
    companion object {
        fun of(input: String): DisplayCommand = values().find { it.value == input } ?: throw AocException("could not map command $input")
    }
}

enum class Pixel(val value: Char) {
    LIT('#');
    companion object {
        val mapper: Map<Char,Pixel> = values().associateBy { it.value }
    }
}

@Serializable
@AocInClass(delimiters = [" +"])
@AocInReplacePatterns(["x=", "", "y=", "", "x", " ", "rotate column", "rotate_column", "rotate row", "rotate_row", "by", ""])
data class AoCInput(
    // rect 3x2
    // rotate column x=1 by 1
    // rotate row y=0 by 4
    @AocInField(0) val cmd: String,
    @AocInField(1) val param1: Int,
    @AocInField(2) val param2: Int
)

