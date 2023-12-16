package mri.advent.y2023.day13

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * --- Day 13 Test:  ---
 */
class Day13Test {

    val sample = "/day13_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(405L, Day13(sample).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(400L, Day13(sample).part2())
    }

    @Test
    fun `test grid rotate`() {
        val grid = parsePattern("""
#.##..##.
..#.##.#.
##......#
##......#
..#.##.#.
..##..##.
#.#.##.#.            
        """.trimIndent().lines()
        )
        val rotated = grid.rotate()
        println(rotated.lines.joinToString("\n") { it})
        //assertEquals('.', rotated.lines[0][8])
    }

    @Test
    fun `test string nbDifferences`() {
        assertEquals(2, "abc".nbDifferences("aef"))
        assertEquals(1, "abc".nbDifferences("abe"))
        assertEquals(0, "abc".nbDifferences("abc"))
    }
}
