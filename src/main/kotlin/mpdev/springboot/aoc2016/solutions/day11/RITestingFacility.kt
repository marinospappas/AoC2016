package mpdev.springboot.aoc2016.solutions.day11

import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import mpdev.springboot.aoc2016.solutions.day11.Generator.*
import mpdev.springboot.aoc2016.solutions.day11.MicroChip.*
import mpdev.springboot.aoc2016.utils.AocException
import mpdev.springboot.aoc2016.utils.SGraph
import mpdev.springboot.aoc2016.utils.aStar
import org.springframework.stereotype.Component
import java.util.*
import kotlin.math.abs

@Component
class RITestingFacility(inputDataReader: InputDataReader): PuzzleSolver(inputDataReader, 11) {

    var states = mutableListOf<RIState>()
    val graph = SGraph<RIState>(getConnected = { state -> state.getNeighbourStates().map { Pair(it,1) }.toSet() },
        heuristic = { state -> state.heuristic() })

    override fun initialize() {
        states.clear()
        states.add(RIState(
            mutableMapOf(
                E to 1, S to 1, s to 1, P to 1, p to 1,
                T to 2, R to 2, r to 2, C to 2, c to 2,
                t to 3
            )))
        states.add(RIState((generators + microchips + E).associateWith { 4 }.toMutableMap()))
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun solvePart1(): Int {
        var moves = 0
        var iterations = 0
        previousStates.clear()
        log.info("start state\n{}", states[0])
        for (i in 0..<states.lastIndex) {
            val minPath = graph.aStar(states[i]) { state -> state == states[i + 1] }
            moves += minPath.minCost
            iterations += minPath.numberOfIterations
            log.info("{} moves to state\n{}", moves, states[i+1])
        }
        log.info("number of iterations: {}", iterations)
        return moves
    }

    override fun solvePart2(): Int = 0

    companion object {
        var generators = setOf(C, P, R, S, T)
        var microchips = setOf(c, p, r, s, t)
        const val E = 'E'
    }
}

val previousStates = mutableSetOf<RIState>()

data class RIState(val state: MutableMap<Any,Int>) {

    fun heuristic() = state.values.sumOf { 4 - it }

    fun getNeighbourStates(): List<RIState> {
        val newStates = mutableListOf<RIState>()
        getNeighbours().forEach {
            val newState = getNewState(it.first, it.second, it.third)
            if (!previousStates.contains(newState)) {
                previousStates.add(newState)
                newStates.add(newState)
            }
        }
        return newStates
    }

    fun getNewState(elements: Set<Any>, from: Int, to: Int): RIState {
        val newState = RIState(this.state.toMutableMap())
        elements.forEach {
            if (abs(to-from) == 1 && newState.state[it] == from)
                newState.state[it] = to
            else
                throw AocException("Cannot move $it from $from to $to - current state: \n$newState")
        }
        return newState
    }

    fun getNeighbours(): Set<Triple<Set<Any>, Int, Int>> {
        val currentLevel = state[RITestingFacility.E] ?: throw AocException("Elevator not found...")
        val generatorsThisLevel = state.filter { it.value == currentLevel && it.key is Generator }.map { it.key as Generator }.toSet()
        val microChipsThisLevel = state.filter { it.value == currentLevel && it.key is MicroChip }.map { it.key as MicroChip }.toSet()
        val neighbours: MutableSet<Triple<Set<Any>, Int, Int>> = mutableSetOf()
        val upLevel = if (currentLevel == 4) -1 else currentLevel + 1
        val downLevel = if (currentLevel == 1) -1 else currentLevel - 1
        // see what can move up
        if (upLevel > 0) {
            getMoveUpSet(currentLevel, upLevel, generatorsThisLevel, microChipsThisLevel, neighbours)
        }
        // see what can move down
        if (downLevel > 0) {
            getMoveDownSet(currentLevel, downLevel, generatorsThisLevel, microChipsThisLevel, neighbours)
        }
        return neighbours
    }

    private fun getMoveUpSet(currentLevel: Int, upLevel: Int, generatorsThisLevel: Set<Generator>, microChipsThisLevel: Set<MicroChip>,
                             neighbours: MutableSet<Triple<Set<Any>, Int, Int>>) {
        // generators
        var generatorsUp: Set<Generator> = setOf()
        outerLoop@ for (g1 in generatorsThisLevel)
            for (g2 in (generatorsThisLevel - g1))
                if (canMove(setOf(g1, g2), currentLevel, upLevel)) {
                    generatorsUp = setOf(g1, g2)
                    break@outerLoop
                }
        // if 2 generators can move up we don't move just one, else we find one that can move up
        if (generatorsUp.isEmpty()) {
            for (g in (generatorsThisLevel))
                if (canMove(setOf(g), currentLevel, upLevel)) {
                    generatorsUp = setOf(g)
                    break
                }
        }
        if (generatorsUp.isNotEmpty())
            neighbours.add(Triple(generatorsUp + RITestingFacility.E, currentLevel, upLevel))

        // microchips
        val microChipsUp = microChipsThisLevel.filter { m -> canMove(m, upLevel) }
        // if two can move up then we move two else just one
        if (microChipsUp.size >= 2)
            neighbours.add(Triple(setOf(RITestingFacility.E, microChipsUp[0], microChipsUp[1]), currentLevel, upLevel))
        else if (microChipsUp.isNotEmpty())
            neighbours.add(Triple(setOf(RITestingFacility.E, microChipsUp[0]), currentLevel, upLevel))

        // generator + microchip pair
        for (g in (generatorsThisLevel))
            if (microChipsThisLevel.contains(g.toMicrochip()) && canMovePair(upLevel)) {
                neighbours.add(Triple(setOf(RITestingFacility.E, g, g.toMicrochip()), currentLevel, upLevel))
                break
            }
    }

    private fun getMoveDownSet(currentLevel: Int, downLevel: Int, generatorsThisLevel: Set<Generator>, microChipsThisLevel: Set<MicroChip>,
                             neighbours: MutableSet<Triple<Set<Any>, Int, Int>>) {
        // generators
        val generatorsDown = generatorsThisLevel.filter { g -> canMove(setOf(g), currentLevel, downLevel) }
        // if 1 generator can move down we don't move two, else we look for a pair of then that can move down
        if (generatorsDown.isNotEmpty())
            neighbours.add(Triple(setOf(generatorsDown.first(), RITestingFacility.E), currentLevel, downLevel))
        else {
            outerLoop@ for (g1 in generatorsThisLevel)
                for (g2 in (generatorsThisLevel - g1))
                    if (canMove(setOf(g1, g2), currentLevel, downLevel)) {
                        neighbours.add(Triple(setOf(g1, g2, RITestingFacility.E), currentLevel, downLevel))
                        break@outerLoop
                    }
        }

        // microchips
        val microChipsDown = microChipsThisLevel.filter { m -> canMove(m, downLevel) }
        // if one can move down then we move one
        if (microChipsDown.isNotEmpty())
            neighbours.add(Triple(setOf(RITestingFacility.E, microChipsDown.first()), currentLevel, downLevel))

        // generator + microchip pair
        for (g in (generatorsThisLevel))
            if (microChipsThisLevel.contains(g.toMicrochip()) && canMovePair(downLevel)) {
                neighbours.add(Triple(setOf(RITestingFacility.E, g, g.toMicrochip()), currentLevel, downLevel))
                break
            }
    }

    private fun canMove(generators: Set<Generator>, from: Int, to: Int): Boolean {
        val generatorsTo = state.entries.filter { it.value == to && it.key is Generator }.map { e -> e.key as Generator }.toSet()
        val otherGeneratorsFrom = state.entries.filter { it.value == from && it.key is Generator }.map { e -> e.key as Generator }
            .toMutableSet() - generators
        val otherMicrochipsTo = state.entries.filter { it.value == to && it.key is MicroChip }.map { e -> e.key as MicroChip }
            .toMutableSet() - generators.map { g -> g.toMicrochip() }.toSet()
        val microShipsFrom = state.entries.filter { it.value == from && it.key is MicroChip }.map { e -> e.key as MicroChip }
        return microShipsFrom.all { m -> isMicroChipSafe(m, otherGeneratorsFrom) }        // can leave level from
                && otherMicrochipsTo.all { m -> isMicroChipSafe(m, generatorsTo + generators) }      // can go to level to
    }

    private fun canMove(microChip: MicroChip, to: Int): Boolean {
        val generatorsTo = state.entries.filter { it.value == to && it.key is Generator }.map { e -> e.key as Generator }
        return generatorsTo.isEmpty() || generatorsTo.any { g -> g == microChip.toGenerator() }
    }

    private fun canMovePair(to: Int): Boolean {
        val elementsTo = state.entries.filter { it.value == to }.map { it.key }
        val generatorsTo = elementsTo.filterIsInstance<Generator>().toSet()
        val microChipsTo = elementsTo.filterIsInstance<MicroChip>().toSet()
        return microChipsTo.all { m -> isMicroChipSafe(m, generatorsTo) }
    }

    private fun isMicroChipSafe(microChip: MicroChip, generators: Set<Generator>) =
        generators.isEmpty() || generators.contains(microChip.toGenerator())

    override fun toString(): String = (1..4)
        .joinToString("\n") { i -> "${5 - i}: ${state.filter { e -> e.value == 5 - i }.map { it.key }}" }
}

enum class Generator {
    C, P, R, S, T,  // prod values
    L, H;           // test values
    fun toMicrochip() = MicroChip.valueOf(this.name.lowercase(Locale.getDefault()))
}

enum class MicroChip {
    c, p, r, s, t,
    l, h;
    fun toGenerator() = Generator.valueOf(this.name.uppercase(Locale.getDefault()))
}

