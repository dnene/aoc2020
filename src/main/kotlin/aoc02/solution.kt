package aoc02

import java.io.File
import java.util.regex.Pattern


val PATTERN = Pattern.compile("(\\d+)-(\\d+) *(\\w): *(\\w*)")

data class Row(
    val minCount: Int,
    val maxCount: Int,
    val char: Char,
    val password: String
) {
    fun isValid() = password.count { it == char }.let { it in minCount..maxCount }
    fun isValid2() = (password[minCount-1] == char).xor(password[maxCount-1] == char)
}

fun String.parse() =
    PATTERN.matcher(this).run {
        if (matches()) {
            Row(group(1).toInt(), group(2).toInt(), group(3).first(), group(4))
        } else null
    }

fun main(args: Array<String>) {
    File("data/inputs/aoc02.txt")
        .readLines()
        .mapNotNull { it.parse() }
        .filter { it.isValid() }
        .let { println(it.size) }

    File("data/inputs/aoc02.txt")
        .readLines()
        .mapNotNull { it.parse() }
        .filter { it.isValid2() }
        .let { println(it.size) }
}