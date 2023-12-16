package mri.advent.y2023.day05

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * --- Day 05 Test:  ---
 */
class Day05Test {

    val sample = "/day05_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(35L, Day05(sample).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(46L, Day05(sample).part2())
    }

    @Test
    fun testSeedToLocaltion() {
        val almanac = parseAlmanac(javaClass.getResource("day05.in")?.readText() ?: throw IllegalArgumentException("Test data not found"))
        assertEquals(47909639, almanac.seedToLocation(3763992295L))
    }

}
