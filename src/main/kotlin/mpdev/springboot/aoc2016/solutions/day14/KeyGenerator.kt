package mpdev.springboot.aoc2016.solutions.day14

import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import mpdev.springboot.aoc2016.utils.Md5
import mpdev.springboot.aoc2016.utils.toHexString
import org.springframework.stereotype.Component

@Component
class KeyGenerator(inputDataReader: InputDataReader): PuzzleSolver(inputDataReader, 14) {

    lateinit var salt: String
    var keys: List<Int> = listOf()

    override fun initialize() {
        salt = inputData[0]
    }

    fun generateKeys( part1or2: Int = 1) {
        md5Cache.clear()
        keys = generateSequence(0, Int::inc)
            .filter { isValidKey(it, if (part1or2 == 1) { s -> Md5.checksum(s).toHexString() } else { s -> stretchedHash(s) }) }
            .take(64).toList()
    }

    fun isValidKey(index: Int,  runMd5: (String) -> String): Boolean {
        val md5Str = getMd5String(salt, index, runMd5)
        var repeatChar: Char
        if (md5Str.containsRepeat3().also { repeatChar = it } != NULL) {
            for (i in index + 1..index + 1000)
                if (getMd5String(salt, i, runMd5).contains(repeatChar.toString().repeat(5)))
                    return true
        }
        return false
    }

    val md5Cache: MutableMap<Int,String> = HashMap(30000)

    fun getMd5String(salt: String, index: Int, runMd5: (String) -> String): String = md5Cache.getOrPut(index) { runMd5("$salt$index") }

    fun stretchedHash(s: String): String {
        var hash = s
        for (i in 0..2016)
            hash = Md5.checksum(hash).toHexString()
        return hash
    }

    fun String.containsRepeat3(): Char {
        for (i in 0 .. this.lastIndex - 2)
            if (this[i+1] == this[i] && this[i+2] == this[i])
                return this[i]
        return NULL
    }

    override fun solvePart1(): Int {
        generateKeys()
        return keys.last()
    }

    override fun solvePart2(): Int {
        generateKeys(2)
        return keys.last()
    }

    companion object {
        const val NULL = 0.toChar()
    }
}