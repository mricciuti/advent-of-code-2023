package mri.advent.y2023.day17

import mri.advent.y2023.BaseDay
import java.util.*

enum class Direction(val x: Int, val y: Int) {
    UP(0, -1),
    RIGHT(1, 0),
    DOWN(0, 1),
    LEFT(-1, 0);

    fun opposite() = Direction.entries.first { it.x == -this.x && it.y == -this.y }
    fun isTurn90(other: Direction) = (this.x + other.x) != 0 && (this.y + other.y != 0)
}

data class Point(val x: Int, val y: Int) {
    fun to(direction: Direction) = Point(x + direction.x, y + direction.y)
    fun neighbors() = Direction.entries.map { this.to(it) }
}

data class Cell(val position: Point, val heatLoss: Int)

// Holder class:    current cell with previous moves from same line
data class CellState(val cell: Cell, val lineMoves: MutableList<Direction> = mutableListOf()) {

    fun nextMoveAllowed(nextDirection: Direction, part2: Boolean = false): Boolean {
        if (this.lineMoves.isEmpty()) {
            return true
        }
        if (lineMoves.last() == nextDirection.opposite()) {
            return false   // cannot reverse direction
        }
        val maxMovesSameLine = if (part2) 10 else 3
        if (lineMoves.last() == nextDirection) {
            return lineMoves.size < maxMovesSameLine  // no more than 3 (or 10)  moves allowed in same line
        }
        // part 2 constraint: cannot turn until moved 4 times same direction
        if (part2 && this.lineMoves.last().isTurn90(nextDirection)) {
            return lineMoves.size > 3
        }
        return true
    }

    private fun lastDirectionEquals(direction: Direction) =
        lineMoves.isNotEmpty() && (lineMoves.last() == direction || lineMoves.last() == direction.opposite())

    fun next(target: Cell, from: Direction): CellState {
        val directions = mutableListOf<Direction>()
        if (lastDirectionEquals(from)) {
            directions.addAll(lineMoves)
        }
        directions.add(from)
        return CellState(target, directions)
    }

    override fun toString() = "${cell.position} [${lineMoves.joinToString(" , ")}]"
}

data class Grid(val cells: List<List<Cell>>) {
    val height = cells.size
    val width = cells[0].size

    private fun inGrid(point: Point) = point.x in 0 until width && point.y in 0 until height
    private fun cellAt(point: Point) = cells[point.y][point.x]

    private fun neighbors(source: Cell) = Direction.entries.map { it to source.position.to(it) }
        .filter { inGrid(it.second) }
        .map { it.first to cellAt(it.second) }

    // pseudo dijkstra with custom constraints
    fun dijkstra(sourcePos: Point, targetPos: Point, part2: Boolean = false): Int {
        val source = cellAt(sourcePos)
        val target = cellAt(targetPos)

        // nodes to be visited
        val queue = PriorityQueue<Pair<CellState, Int>>(compareBy { it.second })
        val visited = mutableSetOf<CellState>()

        // initialization
        queue.offer(CellState(source) to 0)

        while (queue.isNotEmpty()) {
            val current = queue.poll() //  pair ( state(cell, dir)  ,  current heatLoss from start )
            val currentState = current.first

            if (currentState.cell == target) {
                return current.second
            }
            if (visited.contains(currentState)) {
                continue
            }
            visited.add(currentState)

            neighbors(currentState.cell)
                .filter { currentState.nextMoveAllowed(it.first, part2) }
                .forEach { neighbor -> queue.offer(currentState.next(neighbor.second, neighbor.first) to current.second + neighbor.second.heatLoss) }
        }
        throw IllegalStateException("Solution not found!")
    }

    fun minHeatPath(part2: Boolean = false): Int {
        val start = Point(0, 0)
        val target = Point(width - 1, height - 1)
        return dijkstra(start, target, part2)
    }
}

open class Day17(inFile: String = "/day17.in") : BaseDay(inFile) {

    private val grid = Grid(data.lines().mapIndexed { y, s -> s.toCharArray().mapIndexed { x, c -> Cell(Point(x, y), c.digitToInt()) } })

    //817  => time taken: 602 ms
    override fun part1() = grid.minHeatPath()

    // 925 => time taken: 1404 ms
    override fun part2() =  grid.minHeatPath(part2 = true)
    
}
fun main() {
    Day17().run()
}