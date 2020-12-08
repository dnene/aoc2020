package aoc06

import arrow.syntax.collections.tail
import java.io.File

val breakOnEmptyLine: (Pair<List<String>?, List<List<String>>>, String) -> Pair<List<String>?, List<List<String>>> =
    { acc, elem ->
        if (elem.isEmpty()) {
            (null as List<String>? to (acc.first?.let { acc.second + listOf(it) } ?: acc.second))
        } else {
            (acc.first ?: mutableListOf<String>()) + elem to acc.second
        }
    }

fun List<String>.groupByBlankLines(): List<List<String>> =
    fold(null as List<String>? to listOf(), breakOnEmptyLine)
        .let { breakOnEmptyLine(it, "").second }

fun main(args: Array<String>) {
    File("data/inputs/aoc06.txt")
        .readLines()
        .flatMap { it.trim().split(" ") }
        .groupByBlankLines()
        .run {
            map { it.joinToString("").toSet().size }
                .sum()
                .let { println(it) }

            map {
                it.tail().fold(it.first().toSet()) { acc, elem ->
                    acc.intersect(elem.toSet())
                }.size
            }
                .sum()
                .let { println(it)}
        }
}