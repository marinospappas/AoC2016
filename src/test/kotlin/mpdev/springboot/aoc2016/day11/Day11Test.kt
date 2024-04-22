package mpdev.springboot.aoc2016.day11

import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.day11.Generator.*
import mpdev.springboot.aoc2016.solutions.day11.MicroChip.*
import mpdev.springboot.aoc2016.solutions.day11.RIState
import mpdev.springboot.aoc2016.solutions.day11.RITestingFacility
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
        solver.initialize()
        solver.startState = RIState(
            Triple(
                listOf(0, C.bitMap, P.bitMap, 0),
                listOf(c.bitMap + p.bitMap, 0, 0, 0),
                0
            )
        )
        solver.endState = RIState(
            Triple(
                listOf(0, 0, 0, C.bitMap + P.bitMap),
                listOf(0, 0, 0, c.bitMap + p.bitMap),
                3
            )
        )
        RITestingFacility.elementIndexes = RITestingFacility.testIndexes

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
        solver.startState.println()
        println("heuristic")
        solver.startState.heuristic().println()
        println("end state")
        solver.endState.println()
        println("heuristic")
        solver.endState.heuristic().println()
        println("* Start *")
        val newStateIndex = listOf(0,1,4,0,0,0,0,0,0,0,0)
        var curState = solver.startState
        for (i in 0..10) {
            println("ROUND $i\nneighbours")
            val mewStates = curState.getNeighbourStates().also {
                it.forEach { n -> println("----------------"); n.println() }
                println("***********")
            }
            curState = mewStates[newStateIndex[i]]
            println("new state")
            curState.println()
        }
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
        println("NA")
        assert(true)
    }
}

