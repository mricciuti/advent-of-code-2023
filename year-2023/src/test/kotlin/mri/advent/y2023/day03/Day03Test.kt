package mri.advent.y2023.day03

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * --- Day 03 Test:  ---
 */
class Day03Test {

    val sample = "/day03_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(4361, Day03(sample).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(467835, Day03(sample).part2())
    }
}


