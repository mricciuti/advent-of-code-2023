package mri.advent.y2023.day10

import mri.advent.y2023.BaseDay


// Data model -----------------------------------------------------------------

enum class Pipe(val code: Char, val pretty: Char, private val entering: Direction, private val leaving: Direction) {
    VERTICAL('|', '│', Direction.UP, Direction.DOWN),
    HORIZONTAL('-', '─', Direction.LEFT, Direction.RIGHT),
    NORTH_EAST('L', '└', Direction.UP, Direction.RIGHT),
    NORTH_WEST('J', '┘', Direction.LEFT, Direction.UP),
    SOUTH_WEST('7', '┐', Direction.LEFT, Direction.DOWN),
    SOUTH_EAST('F', '┌', Direction.DOWN, Direction.RIGHT),
    START('S', 'S', Direction.UP, Direction.UP); // temporary directions, will be overridden

    fun directions() = listOf(entering, leaving)
    fun has(direction: Direction) = this.entering == direction || this.leaving == direction
}

fun Char.toPipe() = Pipe.entries.firstOrNull { it.code == this }

data class Coord(val x: Int, val y: Int) {
    fun to(direction: Direction) = Coord(x + direction.x, y + direction.y)
    override fun toString() = "$x,$y"
}

enum class Direction(val x: Int, val y: Int) {
    UP(0, -1),
    RIGHT(1, 0),
    DOWN(0, 1),
    LEFT(-1, 0);

    fun opposite() = entries.first { it.x == -this.x && it.y == -this.y }
}

data class Cell(val position: Coord, var type: Pipe? = null)

data class Grid(val cells: List<List<Cell>>) {
    private val height = cells.size
    private val width = cells[0].size
    private val allCells = cells.toList().flatMap { it.toList() }.toList()

    private var start = allCells.first { it.type == Pipe.START }

    init {
        // detect type of pipe at start position
        val connectingDirs = Direction.entries.map { it to start.position.to(it) }
            .filter { inGrid(it.second) }.map { it.first to cellAt(it.second) }
            .filter { it.second.type != null && it.second.type!!.directions().contains(it.first.opposite()) }
            .map { it.first }.toList()
        start.type = Pipe.entries.first { it.directions().containsAll(connectingDirs) }
    }

    private fun inGrid(x: Int, y: Int) = x in 0 until width && y in 0 until height
    private fun inGrid(coord: Coord) = inGrid(coord.x, coord.y)
    private fun cellAt(x: Int, y: Int) = if (inGrid(x, y)) cells[y][x] else throw IllegalArgumentException("Coords out of range $x,$y")
    fun cellAt(pos: Coord): Cell = cellAt(pos.x, pos.y)

    // custom toString functions
    override fun toString() = cells.joinToString("\n") { row -> row.joinToString("") }
    fun toString(mapper: (Cell) -> Char) = cells.joinToString("\n") {
        it.joinToString("") { c -> "${mapper.invoke(c)}" }
    }

    // find cells being on the main loop
    fun findLoop(): Set<Cell> {
        val cellsOnLoop = mutableSetOf<Cell>()
        var current = start
        while (true) {
            cellsOnLoop.add(current)
            val nextOnLoop = current.type!!.directions().map { current.position.to(it) }
                .map { cellAt(it) }.firstOrNull { !cellsOnLoop.contains(it) }
            if (nextOnLoop == null) {
                break
            }
            current = nextOnLoop
        }
        return cellsOnLoop
    }

    // find tiles that are enclosed in the previous loop path
    fun findEnclosedTiles(): List<Cell> {
        val loop = findLoop()
        val enclosedTiles = mutableListOf<Cell>()

        cells.forEach { cellsRow ->

            // Ray-casting algo - find cells within within "walls" in this current row
            var nbCross = 0
            var lastCross: Pipe? = null
            cellsRow.forEach { cell ->
                if (loop.contains(cell)) {
                    val currentPipe = cell.type!!
                    if (currentPipe == Pipe.VERTICAL) {
                        // vertical pipes always trigger cross in/out loop
                        nbCross++
                        lastCross = null
                    } else if (currentPipe != Pipe.HORIZONTAL) {

                        if (lastCross != null) {
                            // have we cross the wall ?
                            // cases:
                            //  ┌─*─┐  or └─*─┘   no crossing
                            //
                            //  ┌─*-┘  or └-*─┐   crossing
                            if (lastCross!!.has(Direction.UP) && currentPipe.has(Direction.DOWN) || lastCross!!.has(Direction.DOWN) && currentPipe.has(Direction.UP)) {
                                nbCross++
                            }
                            lastCross = null
                        } else {
                            lastCross = currentPipe
                        }
                    }
                } else {
                    // cell in loop have crossed walls a odd number of time
                    if (nbCross % 2 == 1) enclosedTiles.add(cell)
                }
            }
        }
        return enclosedTiles
    }
}

class Day10(inFile: String = "/day10.in") : BaseDay(inFile) {

    private fun parseGrid(input: String) = Grid(input.lines().mapIndexed { row, s ->
        s.mapIndexed { col, c -> Cell(Coord(col, row), c.toPipe()) }
    })

    // part 1 : 6697
    override fun part1() = parseGrid(data).findLoop().size / 2

    // part 2 : 423
    override fun part2() = parseGrid(data).findEnclosedTiles().size
}

fun main() {
    Day10().run()
}
