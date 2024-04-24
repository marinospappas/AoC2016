package mpdev.springboot.aoc2016.day15

import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.day15.Disk
import mpdev.springboot.aoc2016.solutions.day15.SpinningDisks
import mpdev.springboot.aoc2016.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day15Test {

    private val day = 15                                     ///////// Update this for a new dayN test
    private lateinit var solver: SpinningDisks           ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")

    @BeforeEach
    fun setup() {
        solver = SpinningDisks(inputDataReader)
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
        assertThat(solver.disks.size).isEqualTo(2)
    }

    @Test
    @Order(3)
    fun `Test timing of slot open`() {
        val expected = listOf(
            listOf(1, 6, 11, 16, 21),
            listOf(1, 3, 5, 7, 9)
        )
        solver.disks.forEach { disk ->
            val times = mutableListOf<Int>()
            for (t in 0 .. 30)
                if (disk.isSlotOpen(t))
                    times.add(t)
            println("disk: ${disk.id}, times for open slot: $times")
            assertThat(times.subList(0, 5)).isEqualTo(expected[disk.id - 1])
        }
    }

    @Test
    @Order(5)
    fun `Test timing of additional disk`() {
        val expected = listOf(0, 11, 22, 33, 44, 55, 66, 77, 88, 99)
        val disk = Disk(3, 11, 0)
        val times = mutableListOf<Int>()
        for (t in 0 .. 200)
            if (disk.isSlotOpen(t))
                times.add(t)
        println("disk: ${disk.id}, times for open slot: $times")
        assertThat(times.subList(0, 10)).isEqualTo(expected)
    }

    @Test
    @Order(4)
    fun `Solves Part 1`() {
        val result = solver.solvePart1().also { it.println() }
        assertThat(result).isEqualTo(5)
    }

    @Test
    @Order(6)
    fun `Solves Part 2`() {
        val result = solver.solvePart2().also { it.println() }
        assertThat(result).isEqualTo(85)
    }
}

