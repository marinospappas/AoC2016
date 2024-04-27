package mpdev.springboot.aoc2016.day22

import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.day22.DiskSpace
import mpdev.springboot.aoc2016.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day22Test {

    private val day = 22                                     ///////// Update this for a new dayN test
    private lateinit var solver: DiskSpace            ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")

    @BeforeEach
    fun setup() {
        DiskSpace.LARGE_DISK = 20
        solver = DiskSpace(inputDataReader)
        solver.initialize()
    }

    @Test
    @Order(1)
    fun `Sets Day correctly`() {
        assertThat(solver.day).isEqualTo(day)
    }

    @Test
    @Order(2)
    fun `Reads Input`() {
        solver.disks.forEach { it.println() }
        solver.grid.print()
        assertThat(solver.disks.size).isEqualTo(9)
    }

    @Test
    @Order(3)
    fun `Finds Viable List`() {
        val result = solver.findViablePairs().onEach { it.println() }
        assertThat(result.size).isEqualTo(7)
    }

    @Test
    @Order(4)
    fun `Solves Part 1`() {
        val result = solver.solvePart1().also { it.println() }
        assertThat(result).isEqualTo(7)
    }

    @Test
    @Order(6)
    fun `Solves Part 2`() {
        val result = solver.solvePart2().also { it.println() }
        assertThat(result).isEqualTo(7)
    }
}

