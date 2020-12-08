package aoc04

import aoc04.Field.Companion.fieldMap
import java.io.File

enum class Field(
    val str: String,
    val validator: ((String) -> Boolean)? = null
) {
    BirthYear("byr", { it.length == 4 && it.all { it.isDigit() } && it.toInt() in (1920..2002) }),
    IssueYear("iyr", { it.length == 4 && it.all { it.isDigit() } && it.toInt() in (2010..2020) }),
    ExpirationYear("eyr", { it.length == 4 && it.all { it.isDigit() } && it.toInt() in (2020..2030) }),
    Height("hgt", {
        when (it.takeLast(2).toString()) {
            "cm" -> {
                it.substring(0, it.length - 2).run { toInt() in (150..193) }
            }
            "in" -> {
                it.substring(0, it.length - 2).run { toInt() in (59..76) }
            }
            else -> false
        }
    }),
    HairColour("hcl", { it.length == 7 && it[0] == '#' && it.substring(1).all { it.isDigit() || it in ('a'..'f') } }),
    EyeColour("ecl", { it in allowedEyeColours }),
    PassportId("pid", { it.length == 9 && it.all { it.isDigit() } }),
    CountryId("cid");

    fun isValid(value: String): Boolean = validator?.let { it(value) } ?: true

    companion object {
        val mandatoryKeys = values().filter { it.validator != null }.map { it.str }.toSet()
        val allowedEyeColours = setOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
        val fieldMap = values().map { it.str to it }.toMap()
    }
}

val breakOnEmptyLine: (Pair<List<String>?, List<List<String>>>, String) -> Pair<List<String>?, List<List<String>>> =
    { acc, elem ->
        if (elem.isEmpty()) {
            (null as List<String>? to (acc.first?.let { acc.second + listOf(it) } ?: acc.second))
        } else {
            (acc.first ?: mutableListOf<String>()) + elem to acc.second
        }
    }

fun main(args: Array<String>) {
    File("data/inputs/aoc04.txt")
        .readLines()
        .flatMap { it.trim().split(" ") }
        .fold(null as List<String>? to listOf(), breakOnEmptyLine)
        .let { breakOnEmptyLine(it, "").second }
        .map { it.map { it.split(":").let { it[0] to it[1] } } }
        .run {
            filter { (Field.mandatoryKeys subtract it.map { it.first }.toSet()).isEmpty() }
                .size
                .let { println(it) }

            filter { keyValueList ->
                (Field.mandatoryKeys subtract keyValueList.map { it.first }.toSet()).isEmpty() &&
                        keyValueList.all { (key, value) ->
                            fieldMap[key]?.isValid(value) ?: false
                        }
            }
                .size
                .let { println(it) }
        }


}