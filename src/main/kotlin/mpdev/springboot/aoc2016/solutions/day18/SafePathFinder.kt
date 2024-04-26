package mpdev.springboot.aoc2016.solutions.day18

import mpdev.springboot.aoc2016.solutions.day18.Tile.*
import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import mpdev.springboot.aoc2016.utils.*
import org.springframework.stereotype.Component

@Component
class SafePathFinder(inputDataReader: InputDataReader): PuzzleSolver(inputDataReader, 18) {

    val grid: Grid<Tile> = Grid(listOf(inputData[0]), Tile.mapper, border = 0)

    override fun initialize() {
        buildGrid()
    }

    fun buildGrid() {
        for (y in 1 .. GRID_SIZE-1) {
            grid.setRowFromList(y, buildGridRow(grid.getRowAsList(y - 1)))
        }
        grid.updateDimensions()
    }

    fun buildGridRow(prevRow: List<Tile>): List<Tile> {
        val rowAbove = prevRow + SAFE
        val dependentTiles = mutableListOf(SAFE, SAFE, rowAbove[0])
        val newRow = mutableListOf<Tile>()
        for (x in prevRow.indices) {
            dependentTiles.removeFirst()
            dependentTiles.add(rowAbove[x+1])
            newRow.add(Tile.of(dependentTiles))
        }
        return newRow
    }

    override fun solvePart1() = grid.countOf(SAFE)

    override fun solvePart2(): Int {
        var row = grid.getRowAsList(0)
        var count = row.count { it == SAFE }
        for (i in 1 .. 399_999)
            row = buildGridRow(row).also { count += it.count { tile -> tile == SAFE } }
        return count
    }

    companion object {
        var GRID_SIZE = 40
    }
}

enum class Tile(val value: Char) {
    TRAP('^'),
    SAFE('.');
    companion object {
        val mapper: Map<Char,Tile> = values().associateBy { it.value }
        private val trapSet = setOf(
            listOf(TRAP, TRAP, SAFE),
            listOf(SAFE, TRAP, TRAP),
            listOf(TRAP, SAFE, SAFE),
            listOf(SAFE, SAFE, TRAP),
        )
        fun of(dependentTiles: List<Tile>): Tile = if (trapSet.contains(dependentTiles)) TRAP else SAFE
    }
}

