package aoc05

import java.io.File

enum class Split(val rowChar: Char, val colChar: Char, val split: (Pair<Int,Int>) -> Pair<Int,Int>) {
    Lower('F', 'L', { range -> ((range.second - range.first+1)/2).let { range.first to range.first + it - 1} }),
    Upper('B', 'R', { range -> ((range.second - range.first+1)/2).let { range.first + it to range.second} });
    companion object {
        private val rowMap = values().map { it.rowChar to it }.toMap()
        private val colMap = values().map { it.colChar to it }.toMap()
        fun getRow(str: String) = str.fold(0 to 127) { acc, elem -> rowMap[elem]!!.split(acc) }.first
        fun getCol(str: String) = str.fold(0 to 7) { acc, elem -> colMap[elem]!!.split(acc) }.first
        fun getSeat(str: String) = getRow(str.substring(0,7)) * 8 + getCol(str.substring(7))
    }
}

fun main(args: Array<String>) {
    File("data/inputs/aoc05.txt")
        .readLines()
        .map { Split.getSeat(it) }
        .run {
            maxOrNull()
            .let { println(it) }

            ((min()!!..max()!!).toSet() subtract toSet()).let { println(it) }
        }
}
