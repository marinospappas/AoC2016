package mpdev.springboot.aoc2016.day01

import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.day01.Day01
import mpdev.springboot.aoc2016.solutions.day01.DistanceCalculator
import mpdev.springboot.aoc2016.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day01Test {

    private val day = 1                                     ///////// Update this for a new dayN test
    private val puzzleSolver = Day01()                      ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")
    private var inputLines: List<String> = inputDataReader.read(day)

    @BeforeEach
    fun setup() {
        puzzleSolver.setDay()
        puzzleSolver.inputData = inputLines
        puzzleSolver.initSolver()
    }

    @Test
    @Order(1)
    fun `Sets Day correctly`() {
        assertThat(puzzleSolver.day).isEqualTo(day)
    }

    @Test
    @Order(2)
    fun `Reads Input ans sets the Directions List`() {
        val distanceCalculator = DistanceCalculator(inputLines)
        distanceCalculator.directions.forEach { it.println() }
        assertThat(distanceCalculator.directions.size).isEqualTo(4)
    }

    @Test
    @Order(3)
    fun `Follows Directions`() {
        val distanceCalculator = DistanceCalculator(inputLines)
        val newPosition = distanceCalculator.findNewPosition()
        val result = newPosition.manhattan(DistanceCalculator.START_POSITION)
        println("New Position $newPosition")
        assertThat(result).isEqualTo(12)
    }

    @Test
    @Order(4)
    fun `Solves Part 1`() {
        assertThat(puzzleSolver.solvePart1().result).isEqualTo("12")
    }

    @Test
    @Order(5)
    fun `Finds Position Visited Twice`() {
        val distanceCalculator = DistanceCalculator(listOf("R8, R4, R4, R8"))
        val newPosition = distanceCalculator.findPositionVisitedTwice()
        val result = newPosition.manhattan(DistanceCalculator.START_POSITION)
        println("New Position $newPosition")
        assertThat(result).isEqualTo(4)
    }

    @Test
    @Order(6)
    fun `Solves Part 2`() {
        puzzleSolver.inputData = listOf("R8, R4, R4, R8")
        puzzleSolver.initSolver()
        assertThat(puzzleSolver.solvePart2().result).isEqualTo("4")
    }
}
