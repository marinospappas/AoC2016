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
    val md5 = Md5()

    override fun initialize() {
        salt = inputData[0]
    }

    fun generateKeys( part1or2: Int = 1) {
        md5Cache.clear()
        keys = generateSequence(0, Int::inc).filter { isValidKey(it, part1or2) }.take(64).toList()
    }

    fun isValidKey(index: Int, part1or2: Int): Boolean {
        val md5Str = getMd5String(salt, index, part1or2)
        var repeatChar: Char
        if (md5Str.containsRepeat3().also { repeatChar = it } != NULL) {
            for (i in index + 1..index + 1000)
                if (getMd5String(salt, i, part1or2).contains(repeatChar.toString().repeat(5)))
                    return true
        }
        return false
    }

    val md5Cache: MutableMap<Int,String> = HashMap(5000)

    fun getMd5String(salt: String, index: Int, part1or2: Int): String = md5Cache.getOrPut(index) {
            if (part1or2 == 1)
                md5.checksum("$salt$index").toHexString()
            else
                stretchedHash("$salt$index")
    }

    fun stretchedHash(s: String): String {
        var hash = s
        for (i in 0..2016)
            hash = md5.checksum(hash).toHexString()
        return hash
    }

    fun String.containsRepeat3(): Char {
        for (i in this.indices)
            if (i < length-2 && this[i+1] == this[i] && this[i+2] == this[i])
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