package mri.advent.y2023.day18

import mri.advent.y2023.BaseDay
import mri.advent.y2023.utils.extractGroups
import kotlin.math.abs


data class Point(val x: Long, val y: Long) {
    fun lineTo(instruction: DigInstruction) = Line(this, this.to(instruction.direction, instruction.steps))
    fun to(direction: Direction, steps: Int = 1) = Point(x + steps * direction.x, y + steps * direction.y)
    fun distanceTo(other: Point) = abs(this.x - other.x) + abs(this.y - other.y)
}

data class Line(val from: Point, val to: Point) {
    val length = from.distanceTo(to).toLong() + 1
}

enum class Direction(val x: Int, val y: Int, val code: Char, val code2: Int) {
    UP(0, -1, 'U', 3),
    RIGHT(1, 0, 'R', 0),
    DOWN(0, 1, 'D', 1),
    LEFT(-1, 0, 'L', 2), ;
}

fun Char.toDirection() = Direction.entries.first { it.code == this }
fun Int.toDirection() = Direction.entries.first { it.code2 == this }

data class DigInstruction(val direction: Direction, val steps: Int)

const val INSTR_REGEX = """([UDLR] )(\d*) \(#(.{5}[0123])\)"""
fun String.toDigInstruction() = with(this.extractGroups(INSTR_REGEX.toRegex(), 3)) {
    DigInstruction(this[0][0].toDirection(), this[1].toInt())
}

const val INSTR_REGEX_PART2 = """[UDLR] \d* \(#(.{5})([0123])\)"""
fun String.toDigInstructionPart2() = with(this.extractGroups(INSTR_REGEX_PART2.toRegex(), 2)) {
    DigInstruction(this.last()[0].digitToInt().toDirection(), this.first().toInt(radix = 16))
}

/**
 * Calculate area of a given polygon using Shoelace formula
 * https://en.wikipedia.org/wiki/Shoelace_formula
 */
fun area(polygon: List<Point>): Long {
    var area = 0L
    (1 until polygon.size).forEach { pointIndex ->
        val (p1, p2) = polygon[pointIndex - 1] to polygon[pointIndex]
        area += p1.x * p2.y - p1.y * p2.x
    }
    return abs(area / 2)
}

/**  --- Day 18:  --- **/
class Day18(inFile: String = "/day18.in") : BaseDay(inFile) {

    fun solve(instructions: List<DigInstruction>): Long {

        // create plot lines
        val lines = buildList<Line> {
            instructions.forEach { instruction ->
                val previous = if (this.isEmpty()) Point(0, 0) else this.last().to
                add(previous.lineTo(instruction))
            }
        }
        val points = lines.map { it.from }.plus(Point(0, 0)) //flatMap { listOf(it.from, it.to) }.toList().dropLast(1)

        /**
         * - step 1 - with Shoelace formula :  calculate polygon area,
         * - step 2 - calculate polygon perimeter (boundary)
         * - step 3 - with Pick's theorem : find points inside the polygon boundary
         * - return sum of inside & boundary points
         */
        val area = area(points)
        println(" area: $area")

        val boundary = lines.sumOf { it.length } - lines.size //  ! point (0,0) occurs twice
        println(" boundary: $boundary")

        //  Pick's theorem : https://en.wikipedia.org/wiki/Pick%27s_theorem
        // A = interior + boundary/2 - 1
        // interior = A - boundary/2 + 1
        val interior = area - boundary / 2 + 1
        println(" interior: $interior")

        return interior + boundary
    }

    override fun part1() = solve(data.lines().map { it.toDigInstruction() })
    override fun part2() = solve(data.lines().map { it.toDigInstructionPart2() })

}

fun main() {
    // part 1 : 70253
    // part 2 : 131265059885080
    Day18().run()
}