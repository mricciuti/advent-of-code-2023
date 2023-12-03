package mri.advent.y2023.day01

import mri.advent.y2023.BaseDay

class Day01(inFile: String = "/day01.in") : BaseDay(inFile) {

    private val digitsAsLetters = arrayOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")

    override fun part1() = data.lines()
        .map { it.toCharArray().filter { it.isDigit() }.map { it.digitToInt() } }
        .sumOf { 10 * it.first() + it.last() }

    override fun part2() = data.lines()
        .map { firstDigit(it) to lastDigit(it) }
        .sumOf { 10 * it.first + it.second }

    private fun firstDigitInString(aString: String, index: Int): Int? {
        val subString = aString.substring(index)
        if (subString.first().isDigit()) return subString.first().digitToInt()
        return digitsAsLetters.filter { subString.startsWith(it) }
            .map { digitsAsLetters.indexOf(it) + 1 }
            .firstOrNull()
    }

    fun firstDigit(line: String): Int {
        for (idx in line.indices) {
            firstDigitInString(line, idx)?.let { return it }
        }
        throw IllegalStateException("digit not found in string $line")
    }

    fun lastDigit(line: String): Int {
        for (idx in line.indices.reversed()) {
            firstDigitInString(line, idx)?.let { return it }
        }
        throw IllegalStateException("digit not found in string $line")
    }
}

fun main() {
    Day01().run()
}