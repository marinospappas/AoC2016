package mpdev.springboot.aoc2016.utils

private var iterations = 0

fun <T: Comparable<T>> SGraph<T>.tsp(start: T, returnToStart: Boolean = false): MinCostPath<T> {
    iterations = 0
    val minCostPath = MinCostPath<T>()
    minCostPath.path = tspMinPath(start, getNodes().toList() - start, mutableListOf(Pair(start, 0)), returnToStart)
    minCostPath.minCost = minCostPath.path.sumOf { it.second }
    minCostPath.numberOfIterations = iterations
    return minCostPath
}

private fun <T: Comparable<T>> SGraph<T>.tspMinPath(start: T,
                                                    destinations: List<T>,
                                                    path: MutableList<Pair<T,Int>>,
                                                    returnToStart: Boolean = false): MutableList<Pair<T,Int>> {
    if (destinations.size == 1) {
        path.add(Pair(destinations.first(), getCost(start, destinations.first())))
        if (returnToStart) // add cost to return to start
            path.add(Pair(path[0].first, getCost(destinations.first(), path[0].first)))
        return path
    }
    var minCost = Pair(path, Int.MAX_VALUE)
    for (dest in destinations) {
        ++iterations
        val thisPath = tspMinPath(dest, destinations - dest, (path + Pair(dest, getCost(start, dest))).toMutableList(), returnToStart)
        if (thisPath.sumOf { it.second } < minCost.second)
            minCost = Pair(thisPath, thisPath.sumOf { it.second })
    }
    return minCost.first
}
