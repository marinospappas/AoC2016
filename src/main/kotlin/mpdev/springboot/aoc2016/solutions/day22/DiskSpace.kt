package mpdev.springboot.aoc2016.solutions.day22

import kotlinx.serialization.Serializable
import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.day22.DiskItem.*
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import mpdev.springboot.aoc2016.utils.*
import org.springframework.stereotype.Component

@Component
class DiskSpace(inputDataReader: InputDataReader): PuzzleSolver(inputDataReader, 22) {

    lateinit var disks: List<Disk>
    lateinit var grid: Grid<DiskItem>

    override fun initialize() {
        val aocInputList: List<AoCInput> = InputUtils(AoCInput::class.java).readAoCInput(inputData)
        disks = aocInputList.map { Disk(Point(it.x, it.y), it.size, it.used) }
        MAX_X = disks.maxOf { it.id.x }
        grid = Grid(disks.map { d -> d.id }, mapper = DiskItem.mapper, border = 0) { p -> DiskItem.from(disks.first { it.id == p }) }
    }

    fun findViablePairs(): List<Pair<Disk,Disk>> {
        val viable = mutableListOf<Pair<Disk,Disk>>()
        for (i in disks.indices)
            for (j in disks.indices)
                if (i != j && disks[i].used > 0 && disks[i].used <= disks[j].free())
                    viable.add(Pair(disks[i], disks[j]))
        return viable
    }

    fun findMinMovesToAccess(): Int {
        val wallStart = grid.getDataPoints().filter { it.value == WALL }.minOf { it.key.x }
        val wallEnd = grid.getDataPoints().filter { it.value == WALL }.maxOf { it.key.x }
        val wallY = grid.findFirst(WALL).y
        val emptyPosition = grid.findFirst(EMPTY)
        val goalPosition = grid.findFirst(GOAL)
        var result = emptyPosition.manhattan(goalPosition)
        if (wallEnd == MAX_X && wallStart < emptyPosition.x && wallY < emptyPosition.y)
            result += 2 * (emptyPosition.x - (wallStart - 1))
        grid.print()
        return result + 5 * (MAX_X - 1)
    }

    override fun solvePart1(): Int = findViablePairs().count()

    override fun solvePart2(): Int = findMinMovesToAccess()

    companion object {
        var LARGE_DISK = 200
        var MAX_X = 0
    }
}

data class Disk(val id: Point, val size: Int, var used: Int) {
    fun free() = size - used
}

enum class DiskItem(val value: Char) {
    START('S'),
    GOAL('G'),
    WALL('#'),
    EMPTY('_'),
    OTHER('.');

    companion object {
        val mapper: Map<Char, DiskItem> = values().associateBy { it.value }
        fun from(d: Disk): DiskItem = when {
            d.id == Point(0, 0) -> START
            d.id == Point(DiskSpace.MAX_X, 0) -> GOAL
            d.size > DiskSpace.LARGE_DISK -> WALL
            d.used == 0 -> EMPTY
            else -> OTHER
        }
    }
}

@Serializable
@AocInClass(skipLines = 2, delimiters = [" +"])
@AocInReplacePatterns(["/dev/grid/node-x", "", "-y", " ", "T", ""])
data class AoCInput(
    // /dev/grid/node-x0-y0     93T   67T    26T   72%
    //                 0  1      2     3
    @AocInField(0) val x: Int,
    @AocInField(1) val y: Int,
    @AocInField(2) val size: Int,
    @AocInField(3) val used: Int
)
