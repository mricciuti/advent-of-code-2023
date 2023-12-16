package mri.advent.y2023.day08

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * --- Day 08 Test:  ---
 */
class Day08Test {

    val sample = "/day08_sample.in"
    val sample2 = "/day08_sample2.in"

    @Test
    fun `test part1`() {
        assertEquals(6L, Day08(sample).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(6L, Day08(sample2).part2())
    }
}
