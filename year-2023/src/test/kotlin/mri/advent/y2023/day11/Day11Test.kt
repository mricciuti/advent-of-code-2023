package mri.advent.y2023.day11

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * --- Day 11 Test:  ---
 */
class Day11Test {

    val sample = "/day11_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(374, Day11(sample).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(1030, Day11(sample).resolve(10))
        assertEquals(8410, Day11(sample).resolve(100))
    }
}
