package mri.advent.y2023.day17

import mri.advent.y2023.BaseDay
import java.util.*

enum class Direction(val x: Int, val y: Int) {
    UP(0, -1),
    RIGHT(1, 0),
    DOWN(0, 1),
    LEFT(-1, 0);

    fun opposite() = Direction.entries.first { it.x == -this.x && it.y == -this.y }
}

data class Point(val x: Int, val y: Int) {
    fun to(direction: Direction) = Point(x + direction.x, y + direction.y)
    fun neighbors() = Direction.entries.map { this.to(it) }
}

data class Cell(val position: Point, val heatLoss: Int)

// Holder class:    current cell with previous moves from same line
data class CellState(val cell: Cell, val lineMoves: MutableList<Direction> = mutableListOf()) {

    fun nextMoveAllowed(nextDirection: Direction): Boolean {
        if (this.lineMoves.isEmpty()) {
            return true
        }
        // cannot reverse direction
        if (lineMoves.last() == nextDirection.opposite()) {
            return false
        }
        // no more than 3 moves allowed in same line
        if (lineMoves.last() == nextDirection) {
            return lineMoves.size < 3
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


    fun debug(message: Any) {
        //println(message)
    }

    // pseudo dijkstra with custom constraints
    fun dijkstra(sourcePos: Point, targetPos: Point): Int {
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

            // found target ?
            if (currentState.cell == target) {
                return current.second
            }
            // already seen ?
            if (visited.contains(currentState)) {
                continue
            }
            visited.add(currentState)

            // enqueue valid next states
            neighbors(currentState.cell)
                .filter { currentState.nextMoveAllowed(it.first) }
                .forEach { neighbor -> queue.offer(currentState.next(neighbor.second, neighbor.first) to current.second + neighbor.second.heatLoss) }
        }
        throw IllegalStateException("Solution not found!")
    }
}

open class Day17(inFile: String = "/day17.in") : BaseDay(inFile) {

    private val grid = Grid(data.lines().mapIndexed { y, s -> s.toCharArray().mapIndexed { x, c -> Cell(Point(x, y), c.digitToInt()) } })

    //817  => time taken: 1078 ms
    override fun part1() = grid.dijkstra(Point(0, 0), Point(grid.width - 1, grid.height - 1))

    override fun part2(): Any {
        return ""
    }
}

fun main() {
    Day17().run()
}