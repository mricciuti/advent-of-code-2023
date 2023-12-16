package mri.advent.y2023.day15

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * --- Day 15 Test:  ---
 */
class Day15Test {

    val sample = "/day15_sample.in"

    @Test
    fun `test hash()`() {
        assertEquals(52, "HASH".hash())
        assertEquals(30, "rn=1".hash())
        assertEquals(253, "cm-".hash())
        assertEquals(97, "qp=3".hash())
    }

    @Test
    fun `test part1`() {
        assertEquals(1320, Day15(sample).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(145, Day15(sample).part2())
    }


}
