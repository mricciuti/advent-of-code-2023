package mri.advent.y2023.day12

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import kotlin.system.measureTimeMillis
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * --- Day 12 Test:  ---
 */
class Day12Test {

    val sample = "/day12_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(21, Day12(sample).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(525152, Day12(sample).part2())
    }

    @Test
    fun `test part2 perfs`() {

        repeat(10) {
            val springs = "??.???.#?? 1,1,2".unfold().toSpringConditions()
            val time = measureTimeMillis {
                assertEquals(645189, springs.possibleArrangements())
            }
            println("time taken : $time")
            assertTrue { time < 50 }
        }
    }

    @Test
    fun testPart2() {
        listOf(
            "???.### 1,1,3" to 1L,
            ".??..??...?##. 1,1,3" to 16384L,
            "?#?#?#?#?#?#?#? 1,3,1,6" to 1L,
            "????.#...#... 4,1,1" to 16L,
            "????.######..#####. 1,6,5" to 2500L,
            "?###???????? 3,2,1" to 506250L
        ).forEach {
            assertEquals(it.second, it.first.unfold().toSpringConditions().possibleArrangements())
        }
    }

    @Test
    fun `currentGroupValid cases`() {

        fun testGroup(conditions: String, groups: String, expected: Boolean) {
            val springRecord = "$conditions $groups".toSpringConditions()
            assertEquals(expected, springRecord.currentGroupValid())
        }
        assertAll(
            "cases",
            // cas valides
            { testGroup("##..", "2", true) },
            { testGroup("##..#.", "2,1", true) },
            { testGroup("##???", "3,1", true) },

            // cas invalides
            { testGroup("###.", "2", false) },
            { testGroup("##.#", "2", false) },
            { testGroup("##.?", "3", false) },
        )
    }


    @Test
    fun `test count arrangements`() {
        assertAll("cases",
            { assertEquals(1, "???.### 1,1,3".toSpringConditions().possibleArrangements()) },
            { assertEquals(4, ".??..??...?##. 1,1,3".toSpringConditions().possibleArrangements()) }
        )
    }

    @Test
    fun `test parse records`() {
        val records = """
???.### 1,1,3
.??..??...?##. 1,1,3
?#?#?#?#?#?#?#? 1,3,1,6
????.#...#... 4,1,1
????.######..#####. 1,6,5
?###???????? 3,2,1                    
        """.trimIndent().lines().map { it.toSpringConditions() }

        records.forEach { record ->
            println("record: $record")
        }
    }

    @Test
    fun `test simple cases without unknown conditions`() {
        val cases = """
#.#.### 1,1,3
.#...#....###. 1,1,3
.#.###.#.###### 1,3,1,6
####.#...#... 4,1,1
#....######..#####. 1,6,5
.###.##....# 3,2,1            
        """.trimIndent().lines().map { it.toSpringConditions() }

        cases.forEach { assertEquals(1, it.possibleArrangements()) }

    }

}
