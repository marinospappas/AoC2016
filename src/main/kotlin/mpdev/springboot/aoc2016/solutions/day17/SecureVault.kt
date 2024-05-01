package mpdev.springboot.aoc2016.solutions.day17

import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.day17.Pixel.*
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import mpdev.springboot.aoc2016.utils.*
import mpdev.springboot.aoc2016.utils.GridUtils.Direction.*
import org.springframework.stereotype.Component

@Component
class SecureVault(inputDataReader: InputDataReader): PuzzleSolver(inputDataReader, 17) {

    var inputCode = inputData[0]
    final val grid = Grid(GRID, Pixel.mapper, border = 0)
    val  graph: SGraph<State> = SGraph(getConnected = {state -> getConnectedNodes(state)})
    val startPosition = grid.findFirst(START)
    val endPosition = grid.findFirst(VAULT)

    fun getConnectedNodes(state: State): Set<Pair<State,Int>> {
        val connected = mutableSetOf<Pair<State,Int>>()
        val hashCode = Md5.checksum(inputCode + state.path).toHexString().toCharArray()
        for (i in DIRECTIONS.indices) {
            val direction = DIRECTIONS[i]
            if (grid.getDataPoint(state.position + direction.increment) != WALL && isOpen(hashCode[i]))
                connected.add(Pair(State(state.position + direction.increment * 2, state.path + direction.toString1()), 1))
        }
        return connected
    }

    override fun solvePart1(): String {
        val minPath = graph.shortestPathBfs(State(startPosition,"")) { state -> state.position == endPosition }
        return pathToString(minPath.map { Pair(it, 1) })
    }

    fun pathToString(path: List<Pair<State,Int>>): String {
        var s = ""
        for (i in 1 .. path.lastIndex) {
            val p1 = path[i-1].first.position
            val p2 = path[i].first.position
            s += GridUtils.Direction.of((p2 - p1) / 2).toString1().first()
        }
        return s
    }

    override fun solvePart2(): Int = graph.longestPathDfs(State(startPosition,"")) { state -> state.position == endPosition }

    companion object {
        val GRID = listOf(
            "#########",
            "#S|.|.|.#",
            "#-#-#-#-#",
            "#.|.|.|.#",
            "#-#-#-#-#",
            "#.|.|.|.#",
            "#-#-#-#-#",
            "#.|.|.|V#",
            "#########"
        )
        val DIRECTIONS = listOf(UP, DOWN, LEFT, RIGHT)
        private val OPEN_SET = setOf('b', 'c', 'd', 'e', 'f')
        fun isOpen(indicator: Char) = indicator in OPEN_SET
    }
}

data class State(val position: Point, val path: String): Comparable<State> {
    override fun compareTo(other: State) = if (this.position == other.position) 0 else -1
}

enum class Pixel(val value: Char) {
    START('S'),
    ROOM('.'),
    WALL('#'),
    DOOR1('|'),
    DOOR2('-'),
    VAULT('V');
    companion object {
        val mapper: Map<Char,Pixel> = values().associateBy { it.value }
    }
}

