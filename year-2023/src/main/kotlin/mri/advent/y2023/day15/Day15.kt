package mri.advent.y2023.day15

import mri.advent.y2023.BaseDay

/**  HASH algorithm */
fun String.hash(): Int {
    var result = 0
    repeat(this.length) {
        result += this[it].code
        result *= 17
        result %= 256
    }
    return result
}

data class Lens(val label: String, var focalLength: Int = 0)

data class Box(val number: Int, val lenses: MutableList<Lens> = mutableListOf()) {
    fun process(step: StepOperation) {
        if (step is Remove) {
            this.lenses.removeAll { it.label == step.label }
        } else if (step is Assign) {
            this.lenses.firstOrNull { it.label == step.label }
                ?.let { it.focalLength = step.focalLength }
                ?: run { lenses.add(Lens(step.label, step.focalLength)) }
        }
    }

    // sum of focusing power of lenses in this box
    fun focusingPower() = lenses.mapIndexed { index, lens -> this.number * (index + 1) * lens.focalLength }.sum()
}

sealed class StepOperation(val label: String)
class Remove(label: String) : StepOperation(label)
class Assign(label: String, val focalLength: Int) : StepOperation(label)

fun String.toStepOperation(): StepOperation {
    return if (this.contains("-")) Remove(this.substringBefore("-"))
    else with(this.split("=")) { Assign(this[0], this[1].toInt()) }
}

/** --- Day 15:  --- */
class Day15(inFile: String = "/day15.in") : BaseDay(inFile) {

    private val steps = data.split(",")

    // 513643
    override fun part1() = steps.sumOf { it.hash() }

    // 265345
    override fun part2(): Any {
        val boxes = Array(256) { Box(it + 1) }
        steps.map { it.toStepOperation() }.forEach {
            boxes[it.label.hash()].process(it)
        }
        return boxes.sumOf { it.focusingPower() }
    }
}

fun main() {
    Day15().run()
}