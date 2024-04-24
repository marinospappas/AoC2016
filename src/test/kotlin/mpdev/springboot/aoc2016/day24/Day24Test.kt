package mpdev.springboot.aoc2016.day24

import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.day24.Sweeper
import mpdev.springboot.aoc2016.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day24Test {

    private val day = 24                                     ///////// Update this for a new dayN test
    private lateinit var solver: Sweeper           ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")

    @BeforeEach
    fun setup() {
        solver = Sweeper(inputDataReader)
        solver.initialize()
    }

    @Test
    @Order(1)
    fun `Sets Day correctly`() {
        assertThat(solver.day).isEqualTo(day)
    }

    @Test
    @Order(2)
    fun `Reads Input and prepares Basic Graph and Super Graph`() {
        solver.grid.print()
        println("Basic Graph:")
        solver.graph1.print()
        assertThat(solver.graph1.getNodes().size).isEqualTo(20)
        println("Locations:")
        solver.locations.println()
        assertThat(solver.locations.size).isEqualTo(5)
        solver.solveGraph1()
        println("Distances:")
        solver.distances.println()
        assertThat(solver.distances.size).isEqualTo(20)
        println("Super Graph:")
        solver.setGraph0()
        solver.graph0.print()
        assertThat(solver.graph0.getNodes().size).isEqualTo(5)
    }

    @Test
    @Order(4)
    fun `Solves Part 1`() {
        val result = solver.solvePart1().also { it.println() }
        assertThat(result).isEqualTo(14)
    }

    @Test
    @Order(6)
    fun `Solves Part 2`() {
        solver.solvePart1()
        val result = solver.solvePart2().also { it.println() }
        assertThat(result).isEqualTo(20)
    }

}

