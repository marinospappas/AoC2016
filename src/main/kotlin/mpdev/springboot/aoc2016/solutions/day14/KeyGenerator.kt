package mpdev.springboot.aoc2016.solutions.day14

import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import mpdev.springboot.aoc2016.utils.Md5
import org.springframework.stereotype.Component

@Component
class KeyGenerator(inputDataReader: InputDataReader): PuzzleSolver(inputDataReader, 14) {

    lateinit var salt: String
    val keys: MutableList<Int> = mutableListOf()
    val md5 = Md5()

    override fun initialize() {
        salt = inputData[0]
    }

    fun generateKeys() {
        keys.clear()
        var index = 0
        while (keys.size <= 64) {
            if (isValidKey(index))
                keys.add(index)
            ++index
        }
    }

    fun isValidKey(index: Int): Boolean {
        val md5Str = getMd5String("$salt$index")
        var repeatChar: Char
        if (md5Str.containsRepeat(3).also { repeatChar = it } != NULL) {
            for (i in index + 1..index + 1000)
                if (getMd5String("$salt$i").contains(repeatChar.toString().repeat(5)))
                    return true
        }
        return false
    }

    val md5Cache: MutableMap<String,String> = mutableMapOf()

    fun getMd5String(s: String): String = md5Cache.getOrPut(s) { md5.toString(md5.checksum(s)) }

    fun String.containsRepeat(n: Int): Char {
        for (c in this.toCharArray())
            if (this.contains(c.toString().repeat(n)))
                return c
        return NULL
    }

    override fun solvePart1(): Int {
        generateKeys()
        return keys.last()
    }

    override fun solvePart2(): Int = 0

    companion object {
        const val NULL = 0.toChar()
    }
}