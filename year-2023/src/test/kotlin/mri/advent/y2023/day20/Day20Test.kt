package mri.advent.y2023.day20

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * --- Day 20 Test:  ---
 */
class Day20Test {

    val sample = "/day20_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(32000000, Day20(sample).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(5, Day20(sample).part2())
    }
}
