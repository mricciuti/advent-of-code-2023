package mri.advent.y2023.day17

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * --- Day 17 Test:  ---
 */
class Day17Test {

    val sample = "/day17_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(102, Day17(sample).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(94, Day17(sample).part2())
    }

    @Test
    fun `models test`() {
        assertEquals(4, Point(1, 1).neighbors().size)
    }

}
