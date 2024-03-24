package mpdev.springboot.aoc2016.input

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File

@Component
class InputDataReader(@Value("\${input.filename.pattern}") var inputFileNamePattern: String) {

    var testInput = listOf<String>()

    fun read(day: Int): List<String>  = testInput.ifEmpty {
        File("$inputFileNamePattern${String.format("%02d",day)}.txt").readLines()
    }

}