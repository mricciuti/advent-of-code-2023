package mri.advent.y2023.day21

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * --- Day 21 Test:  ---
 */
class Day21Test {

    val sample = "/day21_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(16, Day21(sample, 6).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(5, Day21(sample).part2())
    }
}
