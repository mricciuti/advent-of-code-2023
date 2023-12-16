package mri.advent.y2023.utils

import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class CollectionsUtilsTest {

    @Test
    fun `list compareTo`() {
        assertTrue(listOf(1, 3, 5).compareTo(listOf(1, 5, 7)) < 0)
        assertTrue(listOf(1, 8, 5).compareTo(listOf(1, 5, 7)) > 0)
        assertTrue(listOf(1, 8, 5).compareTo(listOf(1, 8, 5)) == 0)

        assertTrue(listOf('a', 'd', 'f').compareTo(listOf('a', 'e', 'e')) < 0)
    }

}