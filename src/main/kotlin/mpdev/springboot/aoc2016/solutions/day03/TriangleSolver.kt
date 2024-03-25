package mpdev.springboot.aoc2016.solutions.day03

import kotlinx.serialization.Serializable
import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import mpdev.springboot.aoc2016.utils.*
import org.springframework.stereotype.Component
import kotlin.system.measureTimeMillis

@Component
class TriangleSolver(inputDataReader: InputDataReader): PuzzleSolver(inputDataReader, 3) {

    val triangles: List<Triple<Int,Int,Int>>
    val triangles2 = mutableListOf<Triple<Int,Int,Int>>()

    init {
        initTime = measureTimeMillis {
            val aocInputList: List<AoCInput> = InputUtils(AoCInput::class.java).readAoCInput(inputData)
            triangles = aocInputList.map { item ->
                Triple(item.len[0], item.len[1], item.len[2])
            }
        }
    }

    fun setTriangles2() {
        triangles2.clear()
        for (i in triangles.indices step(3)) {
            triangles2.add(Triple(triangles[i].first, triangles[i+1].first, triangles[i+2].first))
            triangles2.add(Triple(triangles[i].second, triangles[i+1].second, triangles[i+2].second))
            triangles2.add(Triple(triangles[i].third, triangles[i+1].third, triangles[i+2].third))
        }
    }

    override fun solvePart1(): Int = triangles.count { it.isTriangle() }

    override fun solvePart2(): Int {
        setTriangles2()
        return triangles2.count { it.isTriangle() }
    }

}

fun Triple<Int,Int,Int>.isTriangle() = first + second > third && second + third > first && third + first > second

@Serializable
@AocInClass
data class AoCInput(
    @AocInField(0, delimiters = [" +"]) val len: List<Int>
)
