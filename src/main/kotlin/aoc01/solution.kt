package aoc01

import java.io.File

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
            combinationsOfTwo()
                .filter { it.first + it.second == 2020 }
                .map { println(it); println(it.first * it.second) }
            combinationsOfThree()
                .filter { it.first + it.second + it.third == 2020 }
                .forEach { println(it); println(it.first * it.second * it.third) }
        }
}