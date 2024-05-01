package mpdev.springboot.aoc2016.utils

class CircularList<T>(var data: MutableList<T>) {

    var curPos = 0

    fun size() = data.size

    operator fun get(index: Int) = data[index % data.size]

    operator fun set(index: Int, value: T) {
        data[index] = value
    }
    fun get(index: Long) = data[(index % data.size).toInt()]

    fun indexOf(value: T) = data.indexOf(value)

    fun getCurrent() = data[curPos]

    fun insert(index: Int, value: T) {
        data.add(index, value)
        curPos = index
    }

    fun incrCurPos(incr: Int = 1) {
        curPos = (curPos + incr) % data.size
    }

    fun sublist(length: Int): List<T> {
        val sList = mutableListOf<T>()
        var index = curPos
        for (i in 1..length) {
            sList.add(data[index])
            if (++index > data.lastIndex)
                index = 0
        }
        return sList
    }

    fun toList() = data.toList()

    fun setSubList(sList: List<T>) {
        var index = curPos
        for (i in 0..sList.lastIndex) {
            data[index] = sList[i]
            if (++index > data.lastIndex)
                index = 0
        }
    }

    fun rotateLeft(n: Int) {
        val newStart = n % data.size
        data = (data.subList(newStart, data.size) + data.subList(0, newStart)).toMutableList()
    }

    fun rotateRight(n: Int) {
        val newStart = n % data.size
        val dataReversed = data.toList().reversed()
        data = (dataReversed.subList(newStart, data.size) + dataReversed.subList(0, newStart)).reversed().toMutableList()
    }

    override fun toString() = data.toString()
}