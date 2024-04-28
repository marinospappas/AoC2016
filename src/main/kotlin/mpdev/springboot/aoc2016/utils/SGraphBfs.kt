package mpdev.springboot.aoc2016.utils

inline fun <reified T> SGraph<T>.shortestPathBfs(from: T, isAtEnd: (T) -> Boolean): List<T> {
    var curPath = mutableListOf(from)
    val visited = mutableSetOf(from)
    val queue = ArrayDeque<MutableList<T>>().also { l -> l.add(curPath) }
    while (queue.isNotEmpty()) {
        curPath = queue.removeFirst()
        val lastNode = curPath.last()
        if (isAtEnd(lastNode))   // found path
            return curPath
        getConnected(lastNode).map { it.first }.forEach { connection ->
            if (!curPath.contains(connection) && !visited.contains(connection)) {
                visited.add(connection)
                val newPartialPath = curPath.toMutableList().also { it.add(connection) }
                queue.add(newPartialPath)
            }
        }
    }
    return emptyList()
}

inline fun <reified T> SGraph<T>.allPaths(from: T, isAtEnd: (T) -> Boolean): List<List<T>> {
    val allPaths = mutableListOf<List<T>>()
    val queue = ArrayDeque<MutableList<T>>()
    var curPath = mutableListOf(from)
    queue.add(curPath)
    while (queue.isNotEmpty()) {
        curPath = queue.removeFirst()
        val lastNode = curPath.last()
        if (isAtEnd(lastNode))   // found path
            allPaths.add(curPath)
        else
            getConnected(lastNode).map { it.first }.forEach { connectedNode ->
                if (!curPath.contains(connectedNode)) {
                    val newPartialPath = curPath.toMutableList().also { it.add(connectedNode) }
                    queue.add(newPartialPath)
                }
            }
    }
    return allPaths
}

inline fun <reified T: Comparable<T>> SGraph<T>.getAllConnectedNodes(from: T): Set<T> {
    val visited = mutableSetOf(from)
    val queue = ArrayDeque<T>().also { l -> l.add(from) }
    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()
        getConnected(current).map { it.first }.sortedBy { it }.forEach { connection ->
            if (!visited.contains(connection)) {
                visited.add(connection)
                queue.add(connection)
            }
        }
    }
    return visited
}