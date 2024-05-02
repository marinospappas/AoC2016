package mpdev.springboot.aoc2016.solutions.day19

import mpdev.springboot.aoc2016.input.InputDataReader
import mpdev.springboot.aoc2016.solutions.PuzzleSolver
import mpdev.springboot.aoc2016.utils.AocException
import org.springframework.stereotype.Component
import kotlin.math.floor
import kotlin.math.log2
import kotlin.math.pow

@Component
class WhiteElephantParty(inputDataReader: InputDataReader): PuzzleSolver(inputDataReader, 19) {

    var inputValue = inputData[0].toInt()

    override fun solvePart1(): Int {
        val n = floor(log2(inputValue.toDouble()))
        return 2 * (inputValue - 2.0.pow(n).toInt()) + 1
    }

    override fun solvePart2(): Int {
        val players = setupPlayers()
        var size = players.size
        var current: ListNode = players.first()
        var toRemove = current
        var distance = 0
        do {
            while (distance < size / 2) {
                toRemove = toRemove.next ?: throw AocException("no next in node [$toRemove]")
                ++distance
            }
            val next = toRemove.next ?: throw AocException("no next in node [$toRemove]")
            toRemove.prev!!.next = toRemove.next
            toRemove.next!!.prev = toRemove.prev
            toRemove = next
            current = current.next ?: throw AocException("no next in node [$toRemove]")
            --distance
        }  while (size-- > 1)
        return current.value
    }

    fun setupPlayers(): Array<ListNode> {
        val players = Array(inputValue) { ListNode(value = it + 1) }
        for (i in 1..players.lastIndex) {
            players[i].prev = players[i - 1]
            players[i - 1].next = players[i]
        }
        players.first().prev = players.last()
        players.last().next = players.first()
        return players
    }

    data class ListNode(var prev: ListNode? = null, var next: ListNode? = null, var value: Int = 0)
}


