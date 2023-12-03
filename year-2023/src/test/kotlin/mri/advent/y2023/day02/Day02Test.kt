package mri.advent.y2023.day02

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * --- Day 02 Test:  ---
 */
class Day02Test {

    private val sample = "/day02_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(8, Day02(sample).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(2286, Day02(sample).part2())
    }
}
