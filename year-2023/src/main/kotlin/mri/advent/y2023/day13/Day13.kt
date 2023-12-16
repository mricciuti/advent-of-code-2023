package mri.advent.y2023.day13

import mri.advent.y2023.BaseDay
import mri.advent.y2023.utils.asChunks
import kotlin.math.min

fun String.nbDifferences(other: String) =
    if (length == other.length) this.indices.count { this[it] != other[it] }
    else throw IllegalArgumentException("not same size")

data class Pattern(val lines: List<String>) {
    private val height = lines.size
    private val width = lines[0].length

    fun findReflectionPosition(horizontalScan: Boolean = true, part2: Boolean = false): Long {
        // first try with horizontal scan
        var reflectionIndex = -1
        val nbSmudges = if (part2) 1 else 0
        for (candidate in 1 until lines.size) {
            if (lines[candidate].nbDifferences(lines[candidate - 1]) <= nbSmudges) {
                // test symetry in rest of pattern
                //  eg.  if index == 5 for height = 9 :   compare lines [1..4] (reversed) with lines [5..8]
                val nbLinesToTest = min(candidate, height - candidate)
                val linePairs = (0 until nbLinesToTest)
                    .map { lines[candidate + it] to lines[candidate - 1 - it] }
                    .map { it.first.nbDifferences(it.second) }
                if (linePairs.sum() == nbSmudges) {
                    reflectionIndex = candidate
                    break
                }
            }
        }
        if (reflectionIndex >= 0) {
            if (horizontalScan) return reflectionIndex * 100L
            else return reflectionIndex * 1L
        }
        // second try with vertical search
        if (horizontalScan) {
            return this.rotate().findReflectionPosition(horizontalScan = false, part2)
        }
        // not found
        throw IllegalArgumentException("mirror position not found")
    }

    fun rotate() = Pattern(Array(width) { col -> Array(height) { row -> lines[row][col] }.joinToString("") }.toList())
}

fun parsePattern(data: List<String>) = Pattern(data.map { it }.toList())

class Day13(inFile: String = "/day13.in") : BaseDay(inFile) {

    val patterns = data.asChunks().map { parsePattern(it) }

    // 34993
    override fun part1() = patterns.sumOf { it.findReflectionPosition() }
    // 29341
    override fun part2() = patterns.sumOf { it.findReflectionPosition(part2 = true) }

}

fun main() {
    Day13().run()
}