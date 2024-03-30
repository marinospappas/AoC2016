package mpdev.springboot.aoc2016.day09

import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.day09.PatternDecompressor
import mpdev.springboot.aoc2016.solutions.day09.decompressV1
import mpdev.springboot.aoc2016.solutions.day09.decompressV2
import mpdev.springboot.aoc2016.solutions.day09.decompressedLengthV2
import mpdev.springboot.aoc2016.utils.println
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test

class Day09Test {

    private val day = 8                                     ///////// Update this for a new dayN test
    private lateinit var solver: PatternDecompressor       ///////// Update this for a new dayN test
    private val inputDataReader = InputDataReader("src/test/resources/inputdata/input")

    @BeforeEach
    fun setup() {
        solver = PatternDecompressor(inputDataReader)
    }

    @Test
    @Order(1)
    fun `Sets Day correctly`() {
        assertThat(solver.day).isEqualTo(day)
    }

    @Test
    @Order(2)
    fun `Reads Input`() {
        solver.inputData[0].println()
        assertThat(solver.inputData[0]).isEqualTo("X(8x2)(3x3)ABCY(4x2)JKLMWZ")
    }

    @Test
    @Order(3)
    fun `Decompresses Patterns`() {
        val patterns = listOf(
            "ADVENT",
            "A(1x5)BC",
            "(3x3)XYZ",
            "A(2x2)BCD(2x2)EFG",
            "(6x1)(1x3)A",
            "X(8x2)(3x3)ABCY",
            "X(8x2)(3x3)ABCY(4x2)JKLMWZ"
        )
        val expected = listOf(
            "ADVENT",
            "ABBBBBC",
            "XYZXYZXYZ",
            "ABCBCDEFEFG",
            "(1x3)A",
            "X(3x3)ABC(3x3)ABCY",
            "X(3x3)ABC(3x3)ABCYJKLMJKLMWZ"
        )
        for (i in patterns.indices) {
            val result = patterns[i].decompressV1().also { println("${patterns[i]} -> $it") }
            assertThat(result).isEqualTo(expected[i])
        }
    }

    @Test
    @Order(4)
    fun `Solves Part 1`() {
        val result = solver.solvePart1().also { it.println() }
        assertThat(result).isEqualTo(28)
    }

    @Test
    @Order(3)
    fun `Decompresses Patterns V2`() {
        val patterns = listOf(
            "(3x3)XYZ",
            "X(8x2)(3x3)ABCY",
            "(27x12)(20x12)(13x14)(7x10)(1x12)A",
            "(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN"
        )
        val expected = listOf(
            "XYZXYZXYZ",
            "XABCABCABCABCABCABCY",
            "A".repeat(241920),
            "ABCABCABCXYXYXYPQRSTPQRSTABCABCABCXYXYXYPQRSTPQRSTABCABCABCXYXYXYPQRSTPQRSTXTWOTWOSEVENSEVENSEVENSEVENSEVENSEVENSEVENTWOTWOSEVENSEVENSEVENSEVENSEVENSEVENSEVENTWOTWOSEVENSEVENSEVENSEVENSEVENSEVENSEVENTWOTWOSEVENSEVENSEVENSEVENSEVENSEVENSEVENTWOTWOSEVENSEVENSEVENSEVENSEVENSEVENSEVENTWOTWOSEVENSEVENSEVENSEVENSEVENSEVENSEVENTWOTWOSEVENSEVENSEVENSEVENSEVENSEVENSEVENTWOTWOSEVENSEVENSEVENSEVENSEVENSEVENSEVENTWOTWOSEVENSEVENSEVENSEVENSEVENSEVENSEVEN"
        )
        for (i in patterns.indices) {
            val result = patterns[i].decompressV2().also { println("${patterns[i]} -> $it") }
            assertThat(result).isEqualTo(expected[i])
        }
    }

    @Test
    @Order(3)
    fun `Calculates Length for Decompressed Patterns V2`() {
        val patterns = listOf(
            "(3x3)XYZ",
            "X(8x2)(3x3)ABCY",
            "(27x12)(20x12)(13x14)(7x10)(1x12)A",
            "(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN"
        )
        val expected = listOf(
            "XYZXYZXYZ".length.toLong(),
            "XABCABCABCABCABCABCY".length.toLong(),
            241920L,
            445L
        )
        for (i in patterns.indices) {
            val result = patterns[i].decompressedLengthV2().also { println("${patterns[i]} -> $it") }
            assertThat(result).isEqualTo(expected[i])
        }
    }

    @Test
    @Order(6)
    fun `Solves Part 2`() {
        val result = solver.solvePart2().also { it.println() }
        assertThat(result).isEqualTo(30L)
    }
}

