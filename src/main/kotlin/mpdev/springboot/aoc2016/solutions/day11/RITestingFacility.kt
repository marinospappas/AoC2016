package mpdev.springboot.aoc2016.solutions.day11

import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import mpdev.springboot.aoc2016.solutions.day11.Generator.*
import mpdev.springboot.aoc2016.solutions.day11.MicroChip.*
import mpdev.springboot.aoc2016.utils.AocException
import mpdev.springboot.aoc2016.utils.SGraph
import mpdev.springboot.aoc2016.utils.dijkstra
import org.springframework.stereotype.Component
import java.util.*
import kotlin.math.abs

@Component
class RITestingFacility(inputDataReader: InputDataReader): PuzzleSolver(inputDataReader, 11) {

    var states = mutableListOf<RIState>()
    val graph = SGraph<RIState>(getConnected = { state -> state.getNeighbourStates().map { Pair(it,1) }.toSet() })

    override fun initialize() {
        states.clear()
        states.add(RIState(
            mutableMapOf(
                E to 1, S to 1, s to 1, P to 1, p to 1,
                T to 2, R to 2, r to 2, C to 2, c to 2,
                t to 3
            )))
        /*states.add(RIState(
            mutableMapOf(
                S to 1, s to 1,
                R to 2, r to 2, C to 2, c to 2,
                E to 3, T to 3, t to 3,  P to 3, p to 3,
            )))
        states.add(RIState(
            mutableMapOf(
                S to 1, s to 1,
                R to 2, r to 2, C to 2, c to 2,
                E to 4, T to 4, t to 4,  P to 4, p to 4,
            )))
        states.add(RIState(
            mutableMapOf(
                S to 1, s to 1,
                R to 2, r to 2, C to 2, c to 2,
                E to 4, T to 4, t to 4, P to 4, p to 4, S to 4, s to 4,
            )))
        states.add(RIState(
            mutableMapOf(
                S to 1, s to 1,
                E to 3, R to 3, r to 3, C to 3, c to 3,
                T to 4, t to 4, P to 4, p to 4, S to 4, s to 4,
            )))*/
        states.add(RIState((generators + microchips + E).associateWith { 4 }.toMutableMap()))
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun solvePart1(): Int {
        var moves = 0
        var iterations = 0
        log.info("start state\n{}", states[0])
        for (i in 0..<states.lastIndex) {
            val minPath = graph.dijkstra(states[i]) { state -> state == states[i + 1] }
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

data class RIState(val state: MutableMap<Any,Int>) {

    fun getNeighbourStates() = getNeighbours().map { getNewState(it.first, it.second, it.third) }

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
        val destinations = when (currentLevel) {
            1 -> setOf(2)
            4 -> setOf(3)
            else -> setOf(currentLevel + 1, currentLevel - 1)
        }
        val neighbours: MutableSet<Triple<Set<Any>, Int, Int>> = mutableSetOf()
        // generators
        destinations.forEach { to ->
            generatorsThisLevel.forEach { g1 ->
                generatorsThisLevel.forEach { g2 ->
                    if (canMove(setOf(g1, g2), currentLevel, to))
                        neighbours.add(Triple(setOf(RITestingFacility.E, g1, g2), currentLevel, to))
        } } }
        // chips
        destinations.forEach { to ->
            val microChipsThatCanMove = microChipsThisLevel.filter { m -> canMove(m, to) }.toSet()
            microChipsThatCanMove.forEach { m -> neighbours.add(Triple(setOf(RITestingFacility.E, m), currentLevel, to)) }
            microChipsThatCanMove.forEach { m1 ->
                microChipsThatCanMove.forEach { m2 ->
                    if (m1 != m2) neighbours.add(Triple(setOf(RITestingFacility.E, m1, m2), currentLevel, to))
                }
            }
        }
        // generator + chip
        destinations.forEach { to -> generatorsThisLevel.forEach { g ->
            if (microChipsThisLevel.contains(g.toMicrochip()))
                neighbours.add(Triple(setOf(RITestingFacility.E, g, g.toMicrochip()), currentLevel, to))
        } }
        pruneStates(neighbours)
        return neighbours
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

    private fun isMicroChipSafe(microChip: MicroChip, generators: Set<Generator>) =
        generators.isEmpty() || generators.contains(microChip.toGenerator())

    private fun pruneStates(states: MutableSet<Triple<Set<Any>, Int, Int>>) {
        val copyStates = states.toSet()
        // if we can move 2 items up, we will not move any of them as single item up
        copyStates.filter { it.third > it.second && it.first.size == 3 }.forEach { s ->
            s.first.forEach { i -> states.remove(Triple(setOf(RITestingFacility.E, i), s.second, s.third)) }
        }
        // if we can move any 2 items down individually, we will not move both of them down as a pair
        copyStates.filter { s -> s.third < s.second && s.first.size == 3 }.forEach { s ->
            if ((s.first - RITestingFacility.E).all { i -> copyStates.contains(Triple(setOf(RITestingFacility.E, i), s.second, s.third)) })
                states.remove(s)
        }
        // check for "equivalent states" and keep only one of them
        copyStates.filter { s -> s.first.size == 2 && s.third > s.second && (s.first - RITestingFacility.E).all { it is MicroChip } }
            .also { keepOne(it, states) }
        copyStates.filter { s -> s.first.size == 2 && s.third < s.second && (s.first - RITestingFacility.E).all { it is MicroChip } }
            .also { keepOne(it, states) }
        copyStates.filter { s -> s.first.size == 2 && s.third > s.second && (s.first - RITestingFacility.E).all { it is Generator } }
            .also { keepOne(it, states) }
        copyStates.filter { s -> s.first.size == 2 && s.third < s.second && (s.first - RITestingFacility.E).all { it is Generator } }
            .also { keepOne(it, states) }
        copyStates.filter { s -> s.first.size == 3 && s.third > s.second && (s.first - RITestingFacility.E).all { it is MicroChip } }
            .also { keepOne(it, states) }
        copyStates.filter { s -> s.first.size == 3 && s.third < s.second && (s.first - RITestingFacility.E).all { it is MicroChip } }
            .also { keepOne(it, states) }
        copyStates.filter { s -> s.first.size == 3 && s.third > s.second && (s.first - RITestingFacility.E).all { it is Generator } }
            .also { keepOne(it, states) }
        copyStates.filter { s -> s.first.size == 3 && s.third < s.second && (s.first - RITestingFacility.E).all { it is Generator } }
            .also { keepOne(it, states) }
        copyStates.filter { s -> s.first.size == 3 && s.third > s.second &&
                (s.first - RITestingFacility.E).any { it is Generator } &&  (s.first - RITestingFacility.E).any { it is MicroChip } }
            .also { keepOne(it, states) }
        copyStates.filter { s -> s.first.size == 3 && s.third < s.second &&
                (s.first - RITestingFacility.E).any { it is Generator } &&  (s.first - RITestingFacility.E).any { it is MicroChip } }
            .also { keepOne(it, states) }
        /*println("pruning states:")
        println(copyStates)
        println("pruned to:")
        println(states)*/
    }

    private fun keepOne(equivStates: List<Triple<Set<Any>, Int, Int>>, states: MutableSet<Triple<Set<Any>, Int, Int>>) {
        equivStates.forEach { item ->
            if (item != equivStates.first())
                states.remove(item)
        }
    }

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

