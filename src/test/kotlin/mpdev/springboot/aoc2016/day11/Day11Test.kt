package mpdev.springboot.aoc2016.day11

import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.day11.Generator
import mpdev.springboot.aoc2016.solutions.day11.MicroChip
import mpdev.springboot.aoc2016.solutions.day11.RIState
import mpdev.springboot.aoc2016.solutions.day11.RITestingFacility
import mpdev.springboot.aoc2016.solutions.day11.RITestingFacility.Companion.E
import mpdev.springboot.aoc2016.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day11Test {

    private val day = 11                                     ///////// Update this for a new dayN test
    private lateinit var solver: RITestingFacility           ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")

    @BeforeEach
    fun setup() {
        solver = RITestingFacility(inputDataReader)
        RITestingFacility.generators = setOf(Generator.H, Generator.L)
        RITestingFacility.microchips = setOf(MicroChip.h, MicroChip.l)
        solver.initialize()
        solver.states.clear()
        solver.states.add(RIState(
            mutableMapOf(
                E to 1, MicroChip.h to 1, MicroChip.l to 1,
                Generator.H to 2,
                Generator.L to 3
            )))
        solver.states.add(RIState((RITestingFacility.generators + RITestingFacility.microchips + E).associateWith { 4 }.toMutableMap()))
    }

    @Test
    @Order(1)
    fun `Sets Day correctly`() {
        assertThat(solver.day).isEqualTo(day)
    }

    @Test
    @Order(2)
    fun `Prepares Input and Moves Elements`() {
        println("start state")
        solver.states[0].println()
        println("neighbours")
        solver.states[0].getNeighbours().println()
        println("move elements 1")
        var newState = solver.states[0].getNewState(setOf(E, MicroChip.h), 1, 2)
        newState.println()
        println("neighbours")
        newState.getNeighbours().println()
        println("move elements 2")
        newState = newState.getNewState(setOf(E, Generator.H, MicroChip.h), 2, 3)
        newState.println()
        println("neighbours")
        newState.getNeighbours().println()
        println("move elements 3")
        newState = newState.getNewState(setOf(E, MicroChip.h), 3, 2)
        newState.println()
        println("neighbours")
        newState.getNeighbours().println()
        println("move elements 4")
        newState = newState.getNewState(setOf(E, MicroChip.h), 2, 1)
        newState.println()
        println("neighbours")
        newState.getNeighbours().println()
        println("move elements 5")
        newState = newState.getNewState(setOf(E, MicroChip.h, MicroChip.l), 1, 2)
        newState.println()
        println("neighbours")
        newState.getNeighbours().println()
        println("move elements 6")
        newState = newState.getNewState(setOf(E, MicroChip.h, MicroChip.l), 2, 3)
        newState.println()
        println("neighbours")
        newState.getNeighbours().println()
        println("move elements 7")
        newState = newState.getNewState(setOf(E, MicroChip.h, MicroChip.l), 3, 4)
        newState.println()
        println("neighbours")
        newState.getNeighbours().println()
        println("move elements 8")
        newState = newState.getNewState(setOf(E, MicroChip.h), 4, 3)
        newState.println()
        println("neighbours")
        newState.getNeighbours().println()
        println("move elements 9")
        newState = newState.getNewState(setOf(E, Generator.H, Generator.L), 3, 4)
        newState.println()
        println("neighbours")
        newState.getNeighbours().println()
        newState.getNeighbours().println()
        println("move elements 10")
        newState = newState.getNewState(setOf(E, MicroChip.l), 4, 3)
        newState.println()
        println("neighbours")
        newState.getNeighbours().println()
        println("move elements 11")
        newState = newState.getNewState(setOf(E, MicroChip.l, MicroChip.h), 3, 4)
        newState.println()
    }

    @Test
    @Order(4)
    fun `Solves Part 1`() {
        val result = solver.solvePart1().also { it.println() }
        assertThat(result).isEqualTo(11)
    }

    @Test
    @Order(6)
    fun `Solves Part 2`() {
        val result = solver.solvePart2().also { it.println() }
        assertThat(result).isEqualTo(30)
    }
}

