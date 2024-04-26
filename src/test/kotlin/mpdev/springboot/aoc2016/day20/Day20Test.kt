package mpdev.springboot.aoc2016.day20

import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.day20.AddressSpace
import mpdev.springboot.aoc2016.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day20Test {

    private val day = 20                                     ///////// Update this for a new dayN test
    private lateinit var solver: AddressSpace           ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")

    @BeforeEach
    fun setup() {
        solver = AddressSpace(inputDataReader)
        solver.initialize()
        AddressSpace.ADDRESS_RANGE = LongRange(0, 9)
    }

    @Test
    @Order(1)
    fun `Sets Day correctly`() {
        assertThat(solver.day).isEqualTo(day)
    }

    @Test
    @Order(2)
    fun `Reads Input and Sets Ranges`() {
        solver.addressRanges.forEach { it.println() }
        assertThat(solver.addressRanges.size).isEqualTo(4)
    }

    @Test
    @Order(3)
    fun `Normalises Ranges`() {
        solver.normaliseAddressRanges()
        solver.normalisedRanges.forEach { it.println() }
        assertThat(solver.normalisedRanges.size).isEqualTo(2)
    }

    @Test
    @Order(4)
    fun `Solves Part 1`() {
        solver.normaliseAddressRanges()
        solver.getAllowedAddresses()
        val result = solver.solvePart1().also { it.println() }
        assertThat(result).isEqualTo(3)
    }

    @Test
    @Order(5)
    fun `Calculates Allowed Ranges`() {
        solver.normaliseAddressRanges()
        solver.getAllowedAddresses()
        solver.allowedRanges.onEach { it.println() }
        assertThat(solver.allowedRanges.size).isEqualTo(2)
    }

    @Test
    @Order(6)
    fun `Solves Part 2`() {
        solver.normaliseAddressRanges()
        solver.getAllowedAddresses()
        val result = solver.solvePart2().also { it.println() }
        assertThat(result).isEqualTo(2)
    }
}

