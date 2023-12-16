package mri.advent.y2023.day11

import mri.advent.y2023.BaseDay
import kotlin.math.abs

data class Coord(val x: Long, val y: Long) {
    constructor(xInt: Int, yInt: Int) : this(xInt.toLong(), yInt.toLong())

    fun distanceTo(other: Coord) = abs(this.x - other.x) + abs(this.y - other.y)
}

class Day11(inFile: String = "/day11.in") : BaseDay(inFile) {

    private fun expand(coords: List<Long>, expansionRate: Long): Map<Long, Long> {
        val sortedCoords = coords.toSet().toList().sorted()
        var nbInsertions = 0L
        val coordMapping = mutableMapOf<Long, Long>()
        sortedCoords
            // associate each coord with distance to previous
            .mapIndexed { index, coord -> coord to if (index == 0) coord else coord - sortedCoords[index - 1] - 1 }
            // expand each range between coords
            .forEach { coordAndDiff ->
                nbInsertions += (expansionRate - 1) * coordAndDiff.second
                coordMapping[coordAndDiff.first] = coordAndDiff.first + nbInsertions
            }
        return coordMapping
    }

    fun resolve(expansionRate: Long): Long {
        // positions of stars in the initial galaxy
        val starsPositions = data.lines().flatMapIndexed { rowIndex, row ->
            row.mapIndexed { colIndex, _ -> Coord(colIndex, rowIndex) }.filterIndexed { index, _ -> row[index] == '#' }
        }
        // expand X and Y coords
        val xCoordMapping = expand(starsPositions.map { it.x }, expansionRate)
        val yCoordMapping = expand(starsPositions.map { it.y }, expansionRate)
        val newStarCoors = starsPositions.map { Coord(xCoordMapping[it.x]!!, yCoordMapping[it.y]!!) }

        // create pairs of Stars
        val starPairs = newStarCoors.indices
            .flatMap { first -> newStarCoors.indices.filter { it > first }.map { second -> newStarCoors[first] to newStarCoors[second] } }
        // sum distances of each start pair
        return starPairs.sumOf { it.first.distanceTo(it.second) }
    }

    override fun part1() = resolve(expansionRate = 2)
    override fun part2() = resolve(expansionRate = 1_000_000)
}

fun main() {
    Day11().run()
    //    Part 1: 10165598        => time taken: 27 ms
    //    Part 2: 678728808158    => time taken: 14 ms
}