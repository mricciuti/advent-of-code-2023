package mri.advent.y2023.day16

import mri.advent.y2023.BaseDay

fun <T> Pair<T, T>.reverse() = Pair(this.second, this.first)
val LU = Pair(Direction.LEFT, Direction.UP)    //      \ <-
val LD = Pair(Direction.LEFT, Direction.DOWN)  //      / <-
val RU = Pair(Direction.RIGHT, Direction.UP)   //   -> /
val RD = Pair(Direction.RIGHT, Direction.DOWN) //   -> \
val MIRROR_NW_SE = listOf(LD, LD.reverse(), RU, RU.reverse()) //  /
val MIRROR_SW_NE = listOf(LU, LU.reverse(), RD, RD.reverse()) //  \

enum class Lens(val code: Char, val nextDirection: (direction: Direction) -> List<Direction>) {
    SPLIT_VERT('|', { direction -> if (direction.vertical()) listOf(direction) else listOf(Direction.UP, Direction.DOWN) }),
    SPLIT_HORIZ('-', { direction -> if (direction.horizontal()) listOf(direction) else listOf(Direction.LEFT, Direction.RIGHT) }),
    MIRROR_SE('/', { direction -> listOf(MIRROR_NW_SE.first { it.first == direction }.second) }),
    MIRROR_NE('\\', { direction -> listOf(MIRROR_SW_NE.first { it.first == direction }.second) }),
    EMPTY('.', { direction -> listOf(direction) });
}

fun Char.toLens() = Lens.entries.first { it.code == this }

data class Coord(val x: Int, val y: Int) {
    fun to(direction: Direction) = Coord(x + direction.x, y + direction.y)
}

enum class Direction(val x: Int, val y: Int) {
    UP(0, -1),
    RIGHT(1, 0),
    DOWN(0, 1),
    LEFT(-1, 0);

    fun horizontal() = x != 0
    fun vertical() = y != 0
}

data class Cell(val position: Coord, var lens: Lens) {
    // store incoming beams directions
    private val beamsFrom = mutableSetOf<Direction>()
    fun hasBeamFrom(direction: Direction) = beamsFrom.contains(direction)
    fun addBeamFrom(direction: Direction) {
        beamsFrom.add(direction)
    }

    fun reset() {
        beamsFrom.clear()
    }

    fun isEnergized() = beamsFrom.isNotEmpty()
    override fun toString(): String = if (lens == Lens.EMPTY) beamsFrom.size.toString() else lens.code.toString()
}

data class Grid(val cells: List<List<Cell>>) {
    val height = cells.size
    val width = cells[0].size
    private val allCells = cells.toList().flatMap { it.toList() }.toList()

    private fun inGrid(x: Int, y: Int) = x in 0 until width && y in 0 until height
    private fun inGrid(coord: Coord) = inGrid(coord.x, coord.y)
    private fun cellAt(x: Int, y: Int) = if (inGrid(x, y)) cells[y][x] else throw IllegalArgumentException("Coords out of range $x,$y")
    private fun cellAt(pos: Coord): Cell = cellAt(pos.x, pos.y)

    // a beam passes through the grid from give starting point and direction
    private fun traverse(position: Coord, direction: Direction) {
        if (!inGrid(position)) {
            // stop condition: beam leaves out of grid
            return
        }
        val cell = cellAt(position)
        // cycle detection
        if (cell.hasBeamFrom(direction)) {
            return
        }
        cell.addBeamFrom(direction)
        val nextDirections = cell.lens.nextDirection(direction)
        nextDirections.forEach { dir ->
            val nextPosition = position.to(dir)
            traverse(nextPosition, dir)
        }
    }

    fun traverseAndCountEnergized(startPosition: Coord, startDirection: Direction): Int {
        allCells.forEach { it.reset() }
        traverse(startPosition, startDirection)
        return allCells.count { it.isEnergized() }
    }
}

class Day16(inFile: String = "/day16.in") : BaseDay(inFile) {

    private val grid = Grid(data.lines()
        .mapIndexed { row, s ->
            s.mapIndexed { col, c ->
                Cell(Coord(col, row), c.toLens())
            }
        })

    // 6622
    override fun part1() = grid.traverseAndCountEnergized(Coord(0, 0), Direction.RIGHT)

    // 7130
    override fun part2(): Any {
        val startPosWithDirection = buildList {
            repeat(grid.height) { row ->
                add(Coord(0, row) to Direction.RIGHT)
                add(Coord(grid.width - 1, row) to Direction.LEFT)
            }
            repeat(grid.width) { col ->
                add(Coord(col, 0) to Direction.DOWN)
                add(Coord(col, grid.height - 1) to Direction.UP)
            }
        }
        return startPosWithDirection.maxOf { grid.traverseAndCountEnergized(it.first, it.second) }
    }
}

fun main() {
    Day16().run()
}