package aoc08

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import java.io.File

enum class Operation(val str: String, val accFunc: (Int, Int, Int) -> Pair<Int, Int>) {
    Accumulate("acc", { index, acc, offset -> index + 1 to acc + offset }),
    Jump("jmp", { index, acc, offset -> index + offset to acc }),
    NoOp("nop", { index, acc, _ -> index + 1 to acc });

    companion object {
        val operations = values().map { it.str to it }.toMap()
    }
}

typealias Instructions = List<Pair<Operation, Int>>

data class Processor(val index: Int, val accumulator: Int, val executed: Set<Int>) {
    operator fun invoke(instructions: Instructions): Either<Int, Processor> =
        if (index in executed) {
            accumulator.left()
        } else {
            if (index >= instructions.size) this.right()
            else
                instructions[index]
                    .run {
                        first.accFunc(index, accumulator, second)
                    }
                    .let { Processor(it.first, it.second, executed + index).right() }
        }
}

fun process(processor: Processor, instructions: Instructions): Either<Int, Processor> =
    processor(instructions).fold(
        { it.left() },
        {
            if (processor.index >= instructions.size) it.right()
            else process(it, instructions)
        })

fun main() {
    val instructions = File("data/inputs/aoc08.txt")
        .readLines()
        .map {
            it
                .trim()
                .replace("+", "")
                .split(" ")
                .let { Operation.operations[it[0]]!! to it[1].toInt() }
        }

    // part 1
    println(process(Processor(0, 0, setOf()), instructions))

    instructions.mapIndexedNotNull { index, pair ->
        if (pair.first == Operation.Accumulate) {
            null
        } else {
            val modified = instructions.toMutableList().apply {
                this[index] = (if (pair.first == Operation.Jump) Operation.NoOp else Operation.Jump) to pair.second
            }.toList()
            val result = process(Processor(0, 0, setOf()), modified)
            when (result) {
                is Either.Left -> null
                is Either.Right -> result.b.accumulator
            }
        }
    }.forEach { println(it) }
}