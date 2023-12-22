package mri.advent.y2023.day21

import mri.advent.y2023.BaseDay

data class Coord(val x: Int, val y: Int) {
    val neighbors: List<Coord> by lazy { Direction.entries.map { this.to(it) } }
    fun to(direction: Direction) = Coord(x + direction.x, y + direction.y)

}

enum class Direction(val x: Int, val y: Int) {
    UP(0, -1),
    RIGHT(1, 0),
    DOWN(0, 1),
    LEFT(-1, 0);
}

enum class CellType(val code: Char) { START('S'), GARDEN('.'), ROCK('#') }

fun Char.toCellType() = CellType.entries.first { it.code == this }
data class Cell(val position: Coord, var cellType: CellType) {
    var occupied = false

    override fun toString() = if (occupied) "O" else cellType.toString()
}

data class Map(val cells: List<List<Cell>>) {
    val height = cells.size
    val width = cells.first().size

    val allCells = cells.flatMap { it }

    init {
        allCells.first { it.cellType == CellType.START }.occupied = true
    }

    fun steps(nbSteps: Int) {
        repeat(nbSteps) {
            step()
        }
    }
    fun step() {
        // current occupied positions
        val occupied = allCells.filter { it.occupied }
        allCells.forEach { it.occupied = false }

        // move all
        val moved = occupied.flatMap { it.position.neighbors.filter { inGrid(it) }.map { cellAt(it) }.filter { it.cellType != CellType.ROCK } }
        moved.forEach { it.occupied = true }
        debug("moved ${moved.size} cells")
    }

    private fun inGrid(x: Int, y: Int) = x in 0 until width && y in 0 until height
    private fun inGrid(coord: Coord) = inGrid(coord.x, coord.y)
    private fun cellAt(x: Int, y: Int) = if (inGrid(x, y)) cells[y][x] else throw IllegalArgumentException("Coords out of range $x,$y")
    fun cellAt(pos: Coord): Cell = cellAt(pos.x, pos.y)

    override fun toString() = cells.joinToString("\n") { it.joinToString("") }
}

/** --- Day 21:  --- */
class Day21(inFile: String = "/day21.in", val nbSteps: Int = 64) : BaseDay(inFile) {

    val grid = with(data.lines()) {
        Map(this.mapIndexed { row, s -> s.mapIndexed { col, c -> Cell(Coord(col, row), c.toCellType()) } })
    }

    override fun part1(): Any {
        grid.steps(nbSteps)
        return grid.allCells.count { it.occupied }
    }

    override fun part2(): Any {
        return ""
    }

}

fun main() {
    Day21().run()
}

const val DEBUG = false
fun debug(message: Any) {
    if (DEBUG) println(message)
}