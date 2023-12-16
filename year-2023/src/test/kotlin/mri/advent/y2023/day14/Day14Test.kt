package mri.advent.y2023.day14

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * --- Day 14 Test:  ---
 */
class Day14Test {

    val sample = "/day14_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(136, Day14(sample).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(64, Day14(sample).part2())
    }

    @Test
    fun `test tilt north`() {
        val grid = Day14(sample).parseGrid()
        grid.tiltNorth()

        assertEquals("""
OOOO.#.O..
OO..#....#
OO..O##..O
O..#.OO...
........#.
..#....#.#
..O..#.O.O
..O.......
#....###..
#....#....
        """.trimIndent(), grid.toString())
    }

    @Test
    fun `test tilt south`() {
        val grid = Day14(sample).parseGrid()
        grid.tiltSouth()

        assertEquals("""
.....#....
....#....#
...O.##...
...#......
O.O....O#O
O.#..O.#.#
O....#....
OO....OO..
#OO..###..
#OO.O#...O
        """.trimIndent(), grid.toString())
    }

    @Test
    fun `test tilt west`() {
        val grid = Day14(sample).parseGrid()
        grid.tiltWest()

        assertEquals("""
O....#....
OOO.#....#
.....##...
OO.#OO....
OO......#.
O.#O...#.#
O....#OO..
O.........
#....###..
#OO..#....
        """.trimIndent(), grid.toString())
    }

    @Test
    fun `test tilt east`() {
        val grid = Day14(sample).parseGrid()
        grid.tiltEast()

        assertEquals("""
....O#....
.OOO#....#
.....##...
.OO#....OO
......OO#.
.O#...O#.#
....O#..OO
.........O
#....###..
#..OO#....
        """.trimIndent(), grid.toString())
    }

    @Test
    fun `test tilt cycle`() {
        val grid = Day14(sample).parseGrid()
        grid.cycle()
        assertEquals("""
.....#....
....#...O#
...OO##...
.OO#......
.....OOO#.
.O#...O#.#
....O#....
......OOOO
#...O###..
#..OO#....
        """.trimIndent(), grid.toString())

        grid.cycle()
        grid.cycle()

        assertEquals("""
.....#....
....#...O#
.....##...
..O#......
.....OOO#.
.O#...O#.#
....O#...O
.......OOO
#...O###.O
#.OOO#...O
        """.trimIndent(), grid.toString())
    }

}
