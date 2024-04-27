package mpdev.springboot.aoc2016.solutions.day13

import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.day13.Pixel.*
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import mpdev.springboot.aoc2016.utils.*
import org.springframework.stereotype.Component

@Component
class Maze(inputDataReader: InputDataReader): PuzzleSolver(inputDataReader, 13) {

    val debug = false
    val inputNumber = inputData[0].toInt()
    lateinit var grid: Grid<Pixel>
    lateinit var  graph: SGraph<Point>
    var minX = 0
    var minY = 0
    var maxX = 0
    var maxY = 0

    override fun initialize() {
        grid = Grid(0 .. TARGET.x + TARGET.x / 2,
            0 .. TARGET.y + TARGET.y / 2, Pixel.mapper, border = 0) { x, y -> Pixel.fromXy(x, y, inputNumber) }
        maxX = grid.getMinMaxXY().x2
        maxY = grid.getMinMaxXY().x4
        graph = SGraph(grid.getDataPoints().filter { d -> d.value == PATH }.map {
                d -> Pair(d.key, d.key.adjacentCardinal().filter { grid.getDataPoint(it) == PATH }.toMutableSet())
        })
    }

    override fun solvePart1(): Int {
        val minPath = graph.dijkstra(START) { state -> state == TARGET }
        log.info("{} iterations to {}", minPath.numberOfIterations, TARGET)
        return minPath.minCost
    }

    override fun solvePart2(): Int {
        val targetMap = mutableMapOf<Point,Int>()
        val unreachable = mutableSetOf<Point>()
        if (debug) grid.print()
        for (x in minX .. maxX)
            for (y in minY .. maxY) {
                val target = Point(x, y)
                try {
                    val minPath = graph.dijkstra(START) { state -> state == target }
                    if (debug) log.info("{} iterations to {}, distance: {}", minPath.numberOfIterations, target, minPath.minCost)
                    if (minPath.minCost <= 50)
                        targetMap[target] = minPath.minCost
                }
                catch (e: SGraphException) {
                    unreachable.add(target)
                }
            }
        if (debug && unreachable.isNotEmpty())
            log.info("no path to {} points: {}", unreachable.size, unreachable)
        return targetMap.size
    }

    companion object {
        val START = Point(1,1)
        var TARGET = Point(31, 39)
    }
}

enum class Pixel(val value: Char) {
    WALL('#'),
    PATH('.');
    companion object {
        val mapper: Map<Char,Pixel> = values().associateBy { it.value }
        fun fromXy(x: Int, y: Int, input: Int): Pixel {
            val n = x*x + 3*x + 2*x*y + y + y*y + input
            return if (n.toString(2).toCharArray().count { it == '1' } % 2 == 1) WALL else PATH
        }
    }
}

