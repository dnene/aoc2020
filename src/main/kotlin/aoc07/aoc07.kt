package aoc07

import java.io.File
import java.util.regex.Pattern

data class Bag(val name: String, val contained: List<Pair<Int, String>>) {
    fun has(map: Map<String, Bag>, otherName: String): Boolean =
        contained.any { it.second == otherName || map[it.second]!!.has(map, otherName)}
    fun contains(map: Map<String, Bag>): Int = contained.map {
        it.first * (map[it.second]!!.contains(map) + 1)
    }.sum()
}

fun main() {
    val PATTERN_1 = Pattern.compile("^(\\w* \\w*) bags contain (.*)")
    val PATTERN_2 = Pattern.compile("^(\\d*) (\\w* \\w*) bag.*")
    val bagRules = File("data/inputs/aoc07.txt")
        .readLines()
        .mapNotNull {
            PATTERN_1.matcher(it).let { topLevelMatcher ->
                if (topLevelMatcher.matches() && topLevelMatcher.groupCount() == 2) {
                    topLevelMatcher.group(1) to
                            if (topLevelMatcher.group(2).startsWith("no other bag")) {
                                emptyList()
                            } else {
                                topLevelMatcher.group(2).split(",").map { it.trim() }.mapNotNull {
                                    val lowLevelMatcher = PATTERN_2.matcher(it)
                                    if (lowLevelMatcher.matches()) {
                                        lowLevelMatcher.group(1).toInt() to lowLevelMatcher.group(2)
                                    } else {
                                        null
                                    }
                                }
                            }
                } else null
            }
        }
        .map { Bag(it.first, it.second) }
        .map { it.name to it }
        .toMap()

    bagRules.values.filter { it.has(bagRules,"shiny gold") }.size.let { println(it)}
    bagRules["shiny gold"]!!.contains(bagRules).let { println(it)}
}