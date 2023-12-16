package mri.advent.y2023.utils

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RangeUtilsTest {

    @Test
    fun `LongRange intersects`() {
        assertFalse(LongRange(2, 5).intersects(LongRange(6, 10)))
        assertFalse(LongRange(7, 10).intersects(LongRange(4, 6)))
        assertTrue(LongRange(0, 5).intersects(LongRange(5, 10)))
        assertTrue(LongRange(0, 5).intersects(LongRange(4, 10)))
        assertTrue(LongRange(0, 5).intersects(LongRange(2, 3)))

    }

    @Test
    fun `LongRange instersection`() {
        val sourceRange = LongRange(5, 10)
        assertEquals(LongRange(5, 6), sourceRange.intersection(LongRange(0, 6)))
        assertEquals(LongRange(9, 10), sourceRange.intersection(LongRange(9, 13)))
        assertEquals(VOID_LONG_RANGE, sourceRange.intersection(LongRange(0, 3)))
        assertEquals(VOID_LONG_RANGE, sourceRange.intersection(LongRange(20, 30)))

    }

    @Test
    fun `LongRange cut`() {
        val sourceRange = LongRange(5, 10)
        assertEquals(listOf(sourceRange), sourceRange.cut(LongRange(1, 4)))
        assertEquals(listOf(sourceRange), sourceRange.cut(LongRange(20, 30)))

        assertEquals(listOf(LongRange(8, 10)), sourceRange.cut(LongRange(4, 7)))
        assertEquals(listOf(LongRange(5, 7)), sourceRange.cut(LongRange(8, 12)))

        assertEquals(listOf(LongRange(5, 6), LongRange(9, 10)), sourceRange.cut(LongRange(7, 8)))
    }

}