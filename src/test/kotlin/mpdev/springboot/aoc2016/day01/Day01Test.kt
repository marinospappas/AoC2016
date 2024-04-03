package mpdev.springboot.aoc2016.day01

import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.day01.DistanceCalculator
import mpdev.springboot.aoc2016.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day01Test {

    private val day = 1                                     ///////// Update this for a new dayN test
    private lateinit var solver: DistanceCalculator         ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")

    @BeforeEach
    fun setup() {
        solver = DistanceCalculator(inputDataReader)
        solver.initialize()
    }

    @Test
    @Order(1)
    fun `Sets Day correctly`() {
        assertThat(solver.day).isEqualTo(day)
    }

    @Test
    @Order(2)
    fun `Reads Input ans sets the Directions List`() {
        solver.directions.forEach { it.println() }
        assertThat(solver.directions.size).isEqualTo(4)
    }

    @Test
    @Order(3)
    fun `Follows Directions`() {
        val newPosition = solver.findNewPosition()
        val result = newPosition.manhattan(DistanceCalculator.START_POSITION)
        println("New Position $newPosition")
        assertThat(result).isEqualTo(12)
    }

    @Test
    @Order(4)
    fun `Solves Part 1`() {
        assertThat(solver.solvePart1()).isEqualTo(12)
    }

    @Test
    @Order(5)
    fun `Finds Position Visited Twice`() {
        inputDataReader.testInput = listOf("R8, R4, R4, R8")
        solver = DistanceCalculator(inputDataReader)
        solver.initialize()
        val newPosition = solver.findPositionVisitedTwice()
        val result = newPosition.manhattan(DistanceCalculator.START_POSITION)
        println("New Position $newPosition")
        assertThat(result).isEqualTo(4)
    }

    @Test
    @Order(6)
    fun `Solves Part 2`() {
        inputDataReader.testInput = listOf("R8, R4, R4, R8")
        solver = DistanceCalculator(inputDataReader)
        solver.initialize()
        assertThat(solver.solvePart2()).isEqualTo(4)
    }
}
