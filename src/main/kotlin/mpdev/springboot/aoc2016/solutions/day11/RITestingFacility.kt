package mpdev.springboot.aoc2016.solutions.day11

import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import mpdev.springboot.aoc2016.solutions.day11.Generator.*
import mpdev.springboot.aoc2016.solutions.day11.MicroChip.*
import mpdev.springboot.aoc2016.utils.SGraph
import mpdev.springboot.aoc2016.utils.aStar
import org.springframework.stereotype.Component

@Component
class RITestingFacility(inputDataReader: InputDataReader): PuzzleSolver(inputDataReader, 11) {

    lateinit var startState: RIState
    lateinit var endState: RIState
    val graph = SGraph<RIState>(getConnected = { state -> state.getNeighbourStates().map { Pair(it,1) }.toSet() },
        heuristic = { state -> state.heuristic() })

    override fun initialize() {
        startState = RIState(
            Triple(
                listOf(S.bitMap + P.bitMap, T.bitMap + R.bitMap + C.bitMap, 0, 0),
                listOf(s.bitMap + p.bitMap, r.bitMap + c.bitMap, t.bitMap, 0),
                0
            )
        )
        endState = RIState(
            Triple(
                listOf(0, 0, 0, C.bitMap + P.bitMap + R.bitMap + S.bitMap + T.bitMap),
                listOf(0, 0, 0, c.bitMap + p.bitMap + r.bitMap + s.bitMap + t.bitMap),
                3
            )
        )
        elementIndexes = part1Indexes
    }

    override fun solvePart1(): Int {
        log.info("start state\n{}", startState)
        log.info("end state\n{}", endState)
        previousStates.clear()
        val minPath = graph.aStar(startState) { state -> state == endState }
        log.info("number of iterations: {}", minPath.numberOfIterations)
        log.info("{} moves to endstate", minPath.minCost)
        return minPath.minCost
    }

    override fun solvePart2(): Int {
        elementIndexes = part2Indexes
        val start = startState.state
        startState = RIState(Triple(
            listOf(start.first[0] + E.bitMap + D.bitMap, start.first[1], start.first[2], 0),
            listOf(start.second[0] + e.bitMap + d.bitMap, start.second[1], start.second[2], 0),
            0)
        )
        val end = endState.state
        endState = RIState(Triple(
            listOf(0, 0, 0, end.first[3] + E.bitMap + D.bitMap),
            listOf(0, 0, 0, end.second[3] + e.bitMap + d.bitMap),
            3)
        )
        return solvePart1()
    }

    companion object {
        val part1Indexes = setOf(1,2,4,8,16)
        val part2Indexes = setOf(1,2,4,8,16,32,64)
        val testIndexes = setOf(1,2)
        lateinit var elementIndexes: Set<Int>
    }
}

val previousStates = mutableSetOf<RIState>()

// state is: (A) bit map of the Generators on each floor, (B) bit map of the Microchips on each floor, (C) the Elevator floor
data class RIState(val state: Triple<List<Int>, List<Int>, Int>) {

    fun heuristic(): Int {
        var h = 0
        for (i in 0 .. 2) {
            if (state.first[i] > 0 || state.second[i] > 0)
                h += 1.shl(2 - i)
        }
        return h
    }

    fun getNeighbourStates(): List<RIState> {
        val currentLevel = state.third
        val generatorsThisLevel = state.first[currentLevel]
        val microChipsThisLevel = state.second[currentLevel]
        val neighbours: MutableList<RIState> = mutableListOf()
        val upLevel = if (state.third == 3) -1 else currentLevel + 1
        val downLevel = if (state.third == 0) -1 else currentLevel - 1
        // see what can move up
        if (upLevel >= 0) {
            getMoveUpSet(currentLevel, upLevel, generatorsThisLevel, microChipsThisLevel, neighbours)
        }
        // see what can move down
        if (downLevel >= 0) {
            getMoveDownSet(currentLevel, downLevel, generatorsThisLevel, microChipsThisLevel, neighbours)
        }
        return neighbours
    }

    private fun getMoveUpSet(currentLevel: Int, upLevel: Int, generatorsThisLevel: Int, microChipsThisLevel: Int,
                             neighbours: MutableList<RIState>) {
        // generators
        var generatorsUp = 0
        outerLoop@ for (g1 in RITestingFacility.elementIndexes) {
            if (generatorsThisLevel.and(g1) != 0) {
                for (g2 in RITestingFacility.elementIndexes) {
                    if (generatorsThisLevel.and(g2) != 0 && g2 != g1 && canMoveGenerator(g1 + g2, currentLevel, upLevel)) {
                        generatorsUp = g1 + g2
                        break@outerLoop

                    }
                }
            }
        }
        // if 2 generators can move up we don't move just one, else we find one that can move up
        if (generatorsUp == 0) {
            for (g in RITestingFacility.elementIndexes)
                if (generatorsThisLevel.and(g) != 0 && canMoveGenerator(g, currentLevel, upLevel)) {
                    generatorsUp =g
                    break
                }
        }
        if (generatorsUp != 0) {
            val newGenerators = state.first.toMutableList().also { it[currentLevel] -= generatorsUp }.also { it[upLevel] += generatorsUp }
            addState(neighbours, RIState(Triple(newGenerators, state.second, upLevel)))
        }

        // microchips
        val microchipsSet = mutableListOf<Int>()
        for (i in RITestingFacility.elementIndexes)
            if (microChipsThisLevel.and(i) != 0 && canMoveMicroChip(i, upLevel))
                microchipsSet.add(i)
        // if two can move up then we move two else just one
        val microchipsUp = if (microchipsSet.size >= 2) microchipsSet[0] + microchipsSet[1]
        else if (microchipsSet.isNotEmpty()) microchipsSet[0]
        else 0
        if (microchipsUp > 0) {
            val newMicrochips = state.second.toMutableList().also { it[currentLevel] -= microchipsUp }
                .also { it[upLevel] += microchipsUp }
            addState(neighbours, RIState(Triple(state.first, newMicrochips, upLevel)))
        }

        // generator + microchip pair
        for (g in (RITestingFacility.elementIndexes))
            if (generatorsThisLevel.and(g) != 0 && microChipsThisLevel.and(g) != 0 && areAllMicroChipsSafe(state.second[upLevel], state.first[upLevel] + g)) {
                val newGenerators = state.first.toMutableList().also { it[currentLevel] -= g }.also { it[upLevel] += g }
                val newMicrochips = state.second.toMutableList().also { it[currentLevel] -= g }.also { it[upLevel] += g }
                addState(neighbours, RIState(Triple(newGenerators, newMicrochips, upLevel)))
                break
            }
    }

    private fun getMoveDownSet(currentLevel: Int, downLevel: Int, generatorsThisLevel: Int, microChipsThisLevel: Int,
                             neighbours: MutableList<RIState>) {
        // generators
        var generatorsDown = 0
        val generatorsSet = mutableSetOf<Int>()
        for (i in RITestingFacility.elementIndexes)
            if (generatorsThisLevel.and(i) != 0)
                generatorsSet.add(i)
        // if 1 generator can move down we don't move two, else we look for a pair of then that can move down
        for (g in generatorsSet)
            if (canMoveGenerator(g, currentLevel, downLevel)) {
                generatorsDown = g
                break
            }
        if (generatorsDown == 0) {
            outerLoop@ for (g1 in generatorsSet)
                for (g2 in (generatorsSet - g1))
                    if (canMoveGenerator(g1 + g2, currentLevel, downLevel)) {
                        generatorsDown = g1 + g2
                        break@outerLoop
                    }
        }
        if (generatorsDown != 0) {
            val newGenerators = state.first.toMutableList().also { it[currentLevel] -= generatorsDown }.also { it[downLevel] += generatorsDown }
            addState(neighbours, RIState(Triple(newGenerators, state.second, downLevel)))
        }

        // microchips
        val microchipsSet = mutableListOf<Int>()
        for (i in RITestingFacility.elementIndexes)
            if (microChipsThisLevel.and(i) != 0 && canMoveMicroChip(i, downLevel))
                microchipsSet.add(i)
        // if one can move down then we move one
        if (microchipsSet.isNotEmpty()) {
            val microChipDown = microchipsSet.first()
            val newMicrochips = state.second.toMutableList().also { it[currentLevel] -= microChipDown }
                .also { it[downLevel] += microChipDown }
            addState(neighbours, RIState(Triple(state.first, newMicrochips, downLevel)))
        }

        // generator + microchip pair
        for (g in (generatorsSet))
            if (microChipsThisLevel.and(g) != 0 && areAllMicroChipsSafe(state.second[downLevel], state.first[downLevel] + g)) {
                val newGenerators = state.first.toMutableList().also { it[currentLevel] -= g }.also { it[downLevel] += g }
                val newMicrochips = state.second.toMutableList().also { it[currentLevel] -= g }.also { it[downLevel] += g }
                addState(neighbours, RIState(Triple(newGenerators, newMicrochips, downLevel)))
                break
            }
    }

    private fun addState(newStates: MutableList<RIState>, state: RIState) {
        if (!previousStates.contains(state)) {
            previousStates.add(state)
            newStates.add(state)
        }
    }

    private fun canMoveGenerator(generators: Int, from: Int, to: Int): Boolean {
        val generatorsTo = state.first[to]
        val otherGeneratorsFrom = state.first[from].and(generators.inv())
        val otherMicrochipsTo = state.second[to].and(generators.inv())
        val microShipsFrom = state.second[from]
        return areAllMicroChipsSafe(microShipsFrom, otherGeneratorsFrom) && areAllMicroChipsSafe(otherMicrochipsTo, generatorsTo)
    }

    private fun canMoveMicroChip(microChip: Int, to: Int): Boolean {
        val generatorsTo = state.first[to]
        return generatorsTo == 0 || generatorsTo.and(microChip).xor(microChip) == 0
    }

    private fun areAllMicroChipsSafe(microChips: Int, generators: Int) = generators == 0 || generators.and(microChips).xor(microChips) == 0

    override fun toString(): String = (3 downTo 0)
        .joinToString("\n") { i ->
            var s = "$i: "
            s += if (state.third == i) "EL | " else "   | "
            s += Generator.listFromInt(state.first[i])
            s += " | "
            s += MicroChip.listFromInt(state.second[i])
            "$s |"
        }
}

enum class Generator(val bitMap: Int) {
    C(1), P(2), R(4), S(8), T(16), E(32), D(64);
    companion object {
        fun listFromInt(i: Int) = (0 .. RITestingFacility.elementIndexes.size-1).map { Generator.values()[it] }
            .joinToString(" | ") { v -> if (i.and(v.bitMap) == 0) " " else v.name }
    }

}

enum class MicroChip(val bitMap: Int) {
    c(1), p(2), r(4), s(8), t(16), e(32), d(64);
    companion object {
        fun listFromInt(i: Int) = (0 .. RITestingFacility.elementIndexes.size-1).map { MicroChip.values()[it] }
            .joinToString(" | ") { v -> if (i.and(v.bitMap) == 0) " " else v.name }
    }
}

