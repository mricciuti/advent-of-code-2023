package mri.advent.y2023.day10

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * --- Day 10 Test:  ---
 */
class Day10Test {

    val sample = "/day10_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(4, Day10(sample).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(4, Day10("/day10_sample2.in").part2())
    }

    @Test
    fun `test part2a`() {
        assertEquals(4, Day10("/day10_sample2a.in").part2())
    }

    @Test
    fun `test part3`() {
        assertEquals(8, Day10("/day10_sample2b.in").part2())
    }

    @Test
    fun `test part4`() {
        assertEquals(10, Day10("/day10_sample2c.in").part2())
    }
}