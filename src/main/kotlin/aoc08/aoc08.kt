package aoc08

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import java.io.File

enum class Operation(val str: String, val accFunc: (Int, Int, Int) -> Pair<Int, Int>) {
    Accumulate("acc", { index, acc, offset -> index + 1 to acc + offset }),
    Jump("jmp", { index, acc, offset -> index + offset to acc }),
    NoOp("nop", { index, acc, _ -> index + 1 to acc });

    companion object {
        private val operations = values().map { it.str to it }.toMap()
        operator fun get(str: String) = operations[str]!!
    }
}

typealias Instruction = Pair<Operation, Int>

fun Instruction.step(index: Int, accumulator: Int) = first.accFunc(index, accumulator, second)
fun Pair<Int, Int>.toProcessor(executed: Set<Int>) = Processor(first, second, executed)
fun Operation.switchJumpsNoops() = when (this) {
    Operation.Accumulate -> Operation.Accumulate
    Operation.Jump -> Operation.NoOp
    Operation.NoOp -> Operation.Jump
}

fun List<Instruction>.switchAt(index: Int) =
    toMutableList().apply { set(index, elementAt(index).let { it.first.switchJumpsNoops() to it.second }) }

data class Processor(val index: Int, val accumulator: Int, val executed: Set<Int>) {
    fun step(instructions: List<Instruction>): Either<Processor, Processor> =
        if (index in executed) left()
        else if (index >= instructions.size) this.right()
        else instructions[index].step(index, accumulator).toProcessor(executed + index).right()
}

fun process(processor: Processor, instructions: List<Instruction>): Either<Processor, Processor> =
    processor.step(instructions).flatMap {
        if (processor.index >= instructions.size) it.right()
        else process(it, instructions)
    }

fun main() {
    val instructions = File("data/inputs/aoc08.txt")
        .readLines()
        .map {
            it
                .trim()
                .replace("+", "")
                .split(" ")
                .let { Operation[it[0]]!! to it[1].toInt() }
        }

    // part 1
    process(Processor(0, 0, setOf()), instructions).mapLeft { println(it.accumulator) }

    // part 2
    instructions.mapIndexedNotNull { index, pair ->
        if (pair.first == Operation.Accumulate) {
            null
        } else {
            val result = process(Processor(0, 0, setOf()), instructions.switchAt(index))
            when (result) {
                is Either.Left -> null
                is Either.Right -> result.b.accumulator
            }
        }
    }.forEach { println(it) }
}