package mri.advent.y2023.day09

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * --- Day 09 Test:  ---
 */
class Day09Test {

    val sample = "/day09_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(114L, Day09(sample).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(2L, Day09(sample).part2())
    }
}
