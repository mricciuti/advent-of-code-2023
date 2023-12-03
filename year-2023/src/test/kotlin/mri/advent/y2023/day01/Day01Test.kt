package mri.advent.y2023.day01

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * --- Day 01 Test:  ---
 */
class Day01Test {

    private val sample = "/day01_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(142, Day01(sample).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(281, Day01("/day01_sample-2.in").part2())
    }

    @Test
    fun firstDigitTest() {
        assertEquals(8, Day01().firstDigit("zed8onetwo9"))
    }

    @Test
    fun lastDigit() {
        // 8hkrb3oneightj   === >   8,8
        assertEquals(8, Day01().lastDigit("8hkrb3oneightj"))
    }
}
