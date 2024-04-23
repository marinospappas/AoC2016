package mpdev.springboot.aoc2016.day13

import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.day13.Maze
import mpdev.springboot.aoc2016.solutions.day13.Pixel
import mpdev.springboot.aoc2016.utils.FourComponents
import mpdev.springboot.aoc2016.utils.Point
import mpdev.springboot.aoc2016.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day13Test {

    private val day = 13                                     ///////// Update this for a new dayN test
    private lateinit var solver: Maze           ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")

    @BeforeEach
    fun setup() {
        Maze.TARGET = Point(7,4)
        solver = Maze(inputDataReader)
        solver.initialize()
    }

    @Test
    @Order(1)
    fun `Sets Day correctly`() {
        assertThat(solver.day).isEqualTo(day)
    }

    @Test
    @Order(2)
    fun `Prepares Grid`() {
        solver.grid.print()
        solver.graph.print()
        assertThat(solver.graph.nodes.size).isEqualTo(solver.grid.countOf(Pixel.PATH))
        assertThat(solver.grid.getMinMaxXY()).isEqualTo(FourComponents(0, 10, 0, 6))
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

