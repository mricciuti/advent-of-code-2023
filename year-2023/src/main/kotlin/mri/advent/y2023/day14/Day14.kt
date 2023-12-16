package mri.advent.y2023.day14

import mri.advent.y2023.BaseDay

enum class Tile(val code: Char) { ROUND('O'), ROCK('#'), EMPTY('.') }

fun Char.toTile() = Tile.entries.first { it.code == this }

data class Grid(val tiles: Array<Array<Tile>>) {
    private val height = tiles.size
    private val width = tiles.first().size

    override fun toString() = tiles.joinToString("\n") { it.joinToString("") { it.code.toString() } }

    fun totalLoadAtNorth() = tiles.mapIndexed { index, row -> (height - index) * row.count { it == Tile.ROUND } }.sum()

    fun cycle() {
        tiltNorth()
        tiltWest()
        tiltSouth()
        tiltEast()
    }

    fun tiltNorth() = tiltVertical(this.tiles.indices)
    fun tiltSouth() = tiltVertical(this.tiles.indices.reversed())
    fun tiltEast() = tiltHorizontal(this.tiles.first().indices.reversed())
    fun tiltWest() = tiltHorizontal(this.tiles.first().indices)

    // tilt vertically ( North or South )
    private fun tiltVertical(yRange: IntProgression) {
        repeat(width) { col ->
            var freeTileIndex = yRange.first
            yRange.forEach { row ->
                val current = tiles[row][col]
                if (current == Tile.ROUND) {
                    if (yRange.step * (row - freeTileIndex) > 0) {
                        tiles[row][col] = Tile.EMPTY
                        tiles[freeTileIndex][col] = Tile.ROUND
                    }
                    freeTileIndex += yRange.step
                } else if (current == Tile.ROCK) {
                    freeTileIndex = row + yRange.step
                }
            }
        }
    }

    // tilt horizontally ( West or East )
    private fun tiltHorizontal(xRange: IntProgression) {
        repeat(height) { row ->
            var freeTileIndex = xRange.first
            xRange.forEach { col ->
                val current = tiles[row][col]
                if (current == Tile.ROUND) {
                    if (xRange.step * (col - freeTileIndex) > 0) {
                        tiles[row][col] = Tile.EMPTY
                        tiles[row][freeTileIndex] = Tile.ROUND
                    }
                    freeTileIndex += xRange.step
                } else if (current == Tile.ROCK) {
                    freeTileIndex = col + xRange.step
                }
            }
        }
    }
}

class Day14(inFile: String = "/day14.in") : BaseDay(inFile) {

    fun parseGrid() = Grid(data.lines().map { it.map { it.toTile() }.toTypedArray() }.toTypedArray())

    // 109833 => time taken: 89 ms
    override fun part1() = with(parseGrid()) {
        this.tiltNorth()
        this.totalLoadAtNorth()
    }

    // 99875   => time taken: 176 ms
    override fun part2() = with(parseGrid()) {
        val gridFootPrints = mutableMapOf<String, Int>()
        val maxCycles = 1_000_000_000
        var currentCycle = 0

        while (currentCycle < maxCycles) {
            val footPrint = this.toString()

            if (gridFootPrints.containsKey(footPrint)) {
                val cycleStart = gridFootPrints.get(footPrint)!!
                println(" Cycle detected:  from $cycleStart  to  $currentCycle")
                // skip "cycles of cycles"
                val remainingCycles = maxCycles - currentCycle
                repeat(remainingCycles % (currentCycle - cycleStart)) {
                    this.cycle()
                }
                break
            }
            gridFootPrints.put(footPrint, currentCycle)
            this.cycle()
            currentCycle++
        }
        this.totalLoadAtNorth()
    }

}

fun main() {
    Day14().run()
}
