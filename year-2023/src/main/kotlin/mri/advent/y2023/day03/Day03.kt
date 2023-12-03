package mri.advent.y2023.day03

import mri.advent.y2023.BaseDay

data class Coord(val x: Int, val y: Int)

data class Number(val value: Int, val row: Int, val startX: Int) {
    private val rangeX = startX until startX + value.toString().length
    val neighbors = buildList {
        add(Coord(rangeX.first - 1, row))
        add(Coord(rangeX.last + 1, row))
        (rangeX.first - 1..rangeX.last + 1).forEach {
            add(Coord(it, row - 1))
            add(Coord(it, row + 1))
        }
    }
}

data class Gear(val position: Coord, val numbers: List<Number>) {
    val power = numbers.map { it.value }.reduce { acc, i -> acc * i }
}

data class Schematics(val chars: Array<CharArray>) {
    // detect & store all numbers in grid
    private val numbers = chars.flatMapIndexed { row, line ->
        val partNumbers = mutableListOf<Number>()
        val buffer = mutableListOf<Char>()
        line.forEachIndexed { col, c ->
            if (!c.isDigit()) {
                if (buffer.isNotEmpty()) partNumbers.add(Number(buffer.joinToString("").toInt(), row, col - buffer.size))
                buffer.clear()
            } else {
                buffer.add(c)
            }
        }
        if (buffer.isNotEmpty()) partNumbers.add(Number(buffer.joinToString("").toInt(), row, line.size - buffer.size))
        partNumbers
    }
    // store all symbols with their positon
    private val symbols = chars.flatMapIndexed { row, line ->
        line.mapIndexed { col, c -> Coord(col, row) to c }
            .filter { !it.second.isDigit() && it.second != '.' }
    }.toMap()

    // part 1 : valid part numbers need at least one symbol aroun
    fun partNumbers() = numbers.filter { it.neighbors.any { symbols.keys.contains(it) } }

    // part 2: gears = symbol "*" with exactly two adjacents numbers
    fun gears() = symbols.filter { it.value == '*' }
        .map { symbol -> symbol.key to numbers.filter { it.neighbors.any { it == symbol.key } } }
        .filter { it.second.size == 2 }
        .map { Gear(it.first, it.second) }
}

class Day03(inFile: String = "/day03.in") : BaseDay(inFile) {

    override fun part1(): Any {
        val grid = Schematics(data.lines().map { it.toCharArray() }.toTypedArray())
        return grid.partNumbers().sumOf { it.value }
    }

    override fun part2(): Any {
        val grid = Schematics(data.lines().map { it.toCharArray() }.toTypedArray())
        return grid.gears().sumOf { it.power }
    }

}

fun main() {
    Day03().run()
}
