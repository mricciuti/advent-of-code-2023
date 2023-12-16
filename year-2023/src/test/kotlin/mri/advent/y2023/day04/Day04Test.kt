package mri.advent.y2023.day04

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * --- Day 04 Test:  ---
 */
class Day04Test {

    val day4 = Day04("/day04_sample.in")

    @Test
    fun `test part1`() {
        assertEquals(13, day4.part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(30, day4.part2())
    }

    @Test
    fun `card parsing`() {
        with("Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53".toCard()) {
            assertEquals(5, winningNumbers.size)
            assertEquals(8, myNumbers.size)
            assertEquals(4, matchingNumbers)
        }
    }
}
