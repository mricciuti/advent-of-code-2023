package mri.advent.y2023.utils.maths

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MathsUtilsTest {

    @Test
    fun `leastCommonMultiple test`() {
        assertEquals(12, leastCommonMultiple(3, 4))
        assertEquals(15, leastCommonMultiple(5, 3))
        assertEquals(35, leastCommonMultiple(7, 5))
        assertEquals(72, leastCommonMultiple(24, 18))
    }

    @Test
    fun `List leastCommonMultiple test`() {
        assertEquals(12, listOf(3, 4).map { it.toLong() }.leastCommonMultiple())
        assertEquals(15, listOf(5, 3).map { it.toLong() }.leastCommonMultiple())
        assertEquals(35, listOf(7, 5, 5).map { it.toLong() }.leastCommonMultiple())
        assertEquals(72, listOf(24, 18, 12).map { it.toLong() }.leastCommonMultiple())
    }
}