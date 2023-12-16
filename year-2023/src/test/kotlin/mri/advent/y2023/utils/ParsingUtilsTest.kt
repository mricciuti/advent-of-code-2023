package mri.advent.y2023.utils

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ParsingUtilsTest {

    @Test
    fun `String toInts should remove space chars`() {
        assertEquals(listOf(44, 42, 1, 3), "44 42  1    3 ".toInts())
    }



}