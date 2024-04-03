package mpdev.springboot.aoc2016.solutions.day10

import kotlinx.serialization.Serializable
import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import mpdev.springboot.aoc2016.utils.*
import org.springframework.stereotype.Component
import java.util.*

@Component
class MicrochipFactory(inputDataReader: InputDataReader): PuzzleSolver(inputDataReader, 10) {

    lateinit var robots: Map<Int,Bot>
    lateinit var inputMap: Map<Int,Int>
    val processingQ: Deque<Pair<Int,Int>> = LinkedList()
    val output: MutableMap<Int, MutableList<Int>> = mutableMapOf()

    override fun initialize() {
        val aocInputList1: List<AoCInput> = InputUtils(AoCInput::class.java).readAoCInput(inputData)
        robots = aocInputList1.filter { it.recId == "bot" }.associate {
            it.id to Bot(it.id,
                Pair(Destination.valueOf(it.dest1.replaceFirstChar { c -> if (c.isLowerCase()) c.titlecase() else c.toString() }), it.dest1Id),
                Pair(Destination.valueOf(it.dest2.replaceFirstChar { c -> if (c.isLowerCase()) c.titlecase() else c.toString() }), it.dest2Id))
        }
        inputMap = aocInputList1.filter { it.recId == "value" }.associate { it.id to it.dest1Id }
        inputMap.forEach { (k, v) -> robots[v]?.chips?.add(k) }
        output.forEach { (_,v) -> v.clear() }
    }

    fun runProcess(): Int {
        var resultBot = -1
        var robotsToProcess: List<Bot>
        while (robots.values.filter { b -> b.chips.size == 2 }.also { robotsToProcess = it }.isNotEmpty()) {
            if (resultBot < 0)
                resultBot = findResultBot()
            if (debug) println("result robot: $resultBot")
            // send
            robotsToProcess.forEach { bot ->
                val lowChip = bot.chips.min()
                val highChip = bot.chips.max()
                bot.chips.clear()
                if (bot.outputLow.first == Destination.Bot)
                    processingQ.add(Pair(lowChip, bot.outputLow.second))
                else
                    output.getOrPut(bot.outputLow.second) { mutableListOf() }.add(lowChip)
                if (bot.outputHigh.first == Destination.Bot)
                    processingQ.add(Pair(highChip, bot.outputHigh.second))
                else
                    output.getOrPut(bot.outputHigh.second) { mutableListOf() }.add(highChip)
            }
            if (debug) processingQ.println()
            // receive
            while (processingQ.isNotEmpty()) {
                val chip = processingQ.poll()
                robots[chip.second]?.chips?.add(chip.first)
            }
            if (debug) robots.values.forEach { it.println() }
        }
        if (debug) output.forEach { (k, v) -> println("output $k: $v") }
        return resultBot
    }

    fun findResultBot(): Int =
        robots.values.firstOrNull { b -> b.chips.sorted() == resultChips }?.id ?: -1

    override fun solvePart1(): Int  = runProcess()

    override fun solvePart2(): Int =
        output.entries.filter { it.key <= 2 }.map { it.value.first() }
            .fold(1) { product, next -> product * next }

    companion object {
        var resultChips = listOf(17,61)
        var debug = false
    }
}

enum class Destination {
    Output, Bot
}

data class Bot(val id: Int, val outputLow: Pair<Destination, Int>, val outputHigh: Pair<Destination, Int>,
    var chips: MutableList<Int> = mutableListOf())

@Serializable
@AocInClass(delimiters = [" +"])
@AocInReplacePatterns(["goes to", "", "gives low to", "", "and high to", "", """$""", " -1 -1 -1"])
data class AoCInput(
    // value 5 goes to bot 2
    // 0     1         2   3
    // bot 2 gives low to bot 1 and high to bot 0
    // 0   1              2   3             4   5
    @AocInField(0) val recId: String,
    @AocInField(1) val id: Int,
    @AocInField(2) val dest1: String,
    @AocInField(2) val dest1Id: Int,
    @AocInField(3) val dest2: String,
    @AocInField(3) val dest2Id: Int
)
