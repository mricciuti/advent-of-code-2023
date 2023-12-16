package mri.advent.y2023.day16

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * --- Day 16 Test:  ---
 */
class Day16Test {

    val sample = "/day16_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(46, Day16(sample).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(51, Day16(sample).part2())
    }

    @Test
    fun `test next directions`() {
        var lens = '/'.toLens()
        assertEquals(listOf(Direction.UP), lens.nextDirection(Direction.RIGHT))
        assertEquals(listOf(Direction.RIGHT), lens.nextDirection(Direction.UP))
        assertEquals(listOf(Direction.DOWN), lens.nextDirection(Direction.LEFT))
        assertEquals(listOf(Direction.LEFT), lens.nextDirection(Direction.DOWN))

          lens = '\\'.toLens()
        assertEquals(listOf(Direction.UP), lens.nextDirection(Direction.LEFT))
        assertEquals(listOf(Direction.RIGHT), lens.nextDirection(Direction.DOWN))
        assertEquals(listOf(Direction.DOWN), lens.nextDirection(Direction.RIGHT))
        assertEquals(listOf(Direction.LEFT), lens.nextDirection(Direction.UP))
    }
}
