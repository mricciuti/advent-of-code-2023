package mri.advent.y2023.day06

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * --- Day 06 Test:  ---
 */
class Day06Test {

    val sample = "/day06_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(288, Day06(sample).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(71503, Day06(sample).part2())
    }
}
