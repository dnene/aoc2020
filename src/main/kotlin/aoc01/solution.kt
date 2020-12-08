package aoc01

import java.io.File

fun <T> List<T>.combinations(r: Int, start: Int = 0): List<List<T>> =
    if (r <= size - start) {
        (start..size-start-r).flatMap {
            combinations(r-1, start+1)
        }
    } else listOf<List<T>>()


fun <T> List<T>.combinationsOfTwo(): List<Pair<T, T>> =
    (0..size - 2).flatMap { outer ->
        (outer + 1 until size).map { inner ->
            this[outer] to this[inner]
        }
    }

fun <T> List<T>.combinationsOfThree(): List<Triple<T, T, T>> =
    (0..size - 3).flatMap { outer ->
        (outer + 1..size - 2).flatMap { middle ->
            (middle + 1 until size).map { inner ->
                Triple(this[outer], this[middle], this[inner])
            }
        }
    }

fun main(args: Array<String>) {
    File("data/inputs/aoc01.txt")
        .readLines()
        .map { it.toInt() }
        .run {
            combinations(2)
                .filter { it[0] + it[1] == 2020 }
                .map { println(it); println(it[0] * it[1]) }
            combinations(3)
                .filter { it[0] + it[1] + it[2] == 2020 }
                .forEach { println(it); println(it[0] * it[1] * it[2]) }
        }
}