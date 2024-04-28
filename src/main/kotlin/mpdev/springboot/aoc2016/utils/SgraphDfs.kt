package mpdev.springboot.aoc2016.utils

import java.util.*

inline fun <reified T> SGraph<T>.longestPathDfs__(start: T, isAtEnd: (T) -> Boolean): Int {
    val visited = mutableMapOf<T, Int>()
    val queue = Stack<MutableList<T>>().also { l -> l.add(mutableListOf(start)) }
    while (queue.isNotEmpty()) {
        val curPath = queue.pop()
        if (isAtEnd(curPath.last())) {
            return visited.values.sum()
        }
    }
    return -1
}

//TODO: refactor the below function to use Stack instead of recursion
/*
fun dfsMaxPath(cur: T, isAtEnd: (T) -> Boolean, visited: MutableMap<T, Int>): Int? {
    if (isAtEnd(cur)) {
        return visited.values.sum()
    }
    var maxPath: Int? = null
    getConnected(cur).forEach { (neighbor, steps) ->
        if (neighbor !in visited) {
            visited[neighbor] = steps
            val res = dfsMaxPath(neighbor, isAtEnd, visited)
            if (maxPath == null || (res != null && res > maxPath!!)) {
                maxPath = res
            }
            visited.remove(neighbor)
        }
    }
    return maxPath
}*/
