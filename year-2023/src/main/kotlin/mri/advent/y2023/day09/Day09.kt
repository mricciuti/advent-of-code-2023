package mri.advent.y2023.day09

import mri.advent.y2023.BaseDay
import mri.advent.y2023.utils.toLongs

class Day09(inFile: String = "/day09.in") : BaseDay(inFile) {

    private fun List<Long>.reduce() = (1 until this.size).map { this[it] - this[it - 1] }

    private fun List<Long>.findNext(part2: Boolean = false): Long {
        // reduction phase
        val differences = mutableListOf(this)
        while (differences.last().any { it != 0L }) {
            differences.add(differences.last().reduce())
        }
        // extrapolation phase
        var placeholder = 0L
        (differences.size - 1 downTo 0).forEach { index ->
            placeholder = if (part2) differences[index].first() - placeholder
            else differences[index].last() + placeholder
        }
        return placeholder
    }

    private val histories = data.lines().map { it.toLongs() }

    override fun part1() = histories.sumOf { it.findNext() }
    override fun part2() = histories.sumOf { it.findNext(part2 = true) }
}

fun main() {
    Day09().run()
    /*
        Part 1: 1647269739
          => time taken: 12 ms
        Part 2: 864
          => time taken: 3 ms
     */
}