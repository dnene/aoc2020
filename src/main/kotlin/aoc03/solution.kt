package aoc03

import java.io.File

tailrec fun Array<CharArray>.countTrees(
    slopeRight: Int,
    slopeDown: Int,
    startRow: Int = 0,
    startCol: Int = 0,
    treesSoFar: Int = 0
): Int {
    val newRow = startRow + slopeDown
    return if (newRow > (this.size - 1)) treesSoFar
    else {
        val newCol = (startCol + slopeRight) % this[0].size
        val newTreeCount = if (this[newRow][newCol] == '#') treesSoFar + 1 else treesSoFar
        countTrees(slopeRight, slopeDown, newRow, newCol, newTreeCount)
    }
}

fun main(args: Array<String>) {
    File("data/inputs/aoc03.txt")
        .readLines()
        .map { it.toCharArray() }
        .toTypedArray()
        .run {
            // Note: Part 1
            countTrees(3,1)
                .let { println(it) }

            // Note: Part 2
            listOf((1 to 1), (3 to 1), (5 to 1), (7 to 1), (1 to 2))
                .map { countTrees(it.first, it.second) }
                .fold(1L) { acc, elem -> acc * elem }
                .let { println(it) }
        }
}