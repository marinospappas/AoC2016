package mpdev.springboot.aoc2016.solutions.day24

import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import mpdev.springboot.aoc2016.solutions.day24.Pixel.*
import mpdev.springboot.aoc2016.utils.*
import org.springframework.stereotype.Component

@Component
class Sweeper(inputDataReader: InputDataReader): PuzzleSolver(inputDataReader, 24) {

    final val grid: Grid<Pixel> = Grid(inputData, Pixel.mapper, border = 0)
    val graph1 = SGraph<Point>()
    val graph0 = SGraph<Pixel>()
    val locations = grid.getDataPoints().entries.filter { it.value in (_0.._9) }. map { Pair(it.value, it.key) }
    val distances = mutableMapOf<Pair<Pixel,Pixel>, Int>()

    override fun initialize() {
        grid.getDataPoints().filter { it.value != WALL }.forEach { (k, _) ->
            graph1.addNode(k, k.adjacentCardinal().filter { grid.getDataPoint(it) != WALL }.toSet())
        }
    }

    fun solveGraph1() {
        for (i in locations.indices)
            for (j in i+1 .. locations.lastIndex) {
                distances[Pair(locations[i].first, locations[j].first)] =
                    graph1.dijkstra(locations[i].second) { state -> state == locations[j].second }.minCost
                distances[Pair(locations[j].first, locations[i].first)] = distances[Pair(locations[i].first, locations[j].first)]
                    ?: throw AocException("unexpected error 24001")
            }
    }

    fun setGraph0() {
        locations.map { it.first } .forEach { loc ->
            graph0.addNode(loc,
                distances.entries.filter { e -> e.key.first == loc }.associate { e -> e.key.second to e.value }
            )
        }
    }

    override fun solvePart1(): Int {
        solveGraph1()
        setGraph0()
        val minCostPath = graph0.tsp(START)
        log.info("path 1: {}, number of iterations: {}", minCostPath.path, minCostPath.numberOfIterations)
        return minCostPath.minCost
    }

    override fun solvePart2(): Int {
        val minCostPath = graph0.tsp(START, true)
        log.info("path 2: {}, number of iterations: {}", minCostPath.path, minCostPath.numberOfIterations)
        return minCostPath.minCost
    }

    companion object {
        val START = _0
    }
}

enum class Pixel(val value: Char) {
    WALL('#'), PATH('.'),
    _0('0'), _1('1'), _2('2'), _3('3'), _4('4'),
    _5('5'), _6('6'), _7('7'), _8('8'), _9('9');
    companion object {
        val mapper: Map<Char,Pixel> = values().associateBy { it.value }
    }
}

