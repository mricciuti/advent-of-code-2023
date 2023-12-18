package mri.advent.y2023.day18

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * --- Day 18 Test:  ---
 */
class Day18Test {

    val sample = "/day18_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(62, Day18(sample).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(952408144115, Day18(sample).part2())
    }

    @Test
    fun `parse instruction`() {
        assertEquals(DigInstruction(Direction.RIGHT, 6), "R 6 (#70c710)".toDigInstruction())
        assertEquals(DigInstruction(Direction.UP, 10), "U 10 (#02c563)".toDigInstruction())
    }

    @Test
    fun `parse instruction part 2`() {
        assertEquals(DigInstruction(Direction.RIGHT, 461937), "R 6 (#70c710)".toDigInstructionPart2())
    }

}
