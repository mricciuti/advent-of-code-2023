package mri.advent.y2023.day19

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * --- Day 19 Test:  ---
 */
class Day19Test {

    val sample = "/day19_sample.in"

    @Test
    fun `test parse part`() {
        with("{x=787,m=2655,a=1222,s=2876}".parseXmasPart()) {
            assertEquals(787, this.ratings[Category.X])
            assertEquals(2655, this.ratings[Category.M])
            assertEquals(1222, this.ratings[Category.A])
            assertEquals(2876, this.ratings[Category.S])
        }
    }

    @Test
    fun `test parse workflow`() {
        val str = "px{a<2006:qkq,m>2090:A,rfg}"

        val workflow = str.parseWorkflow()
        assertEquals("px", workflow.name)
    }

    @Test
    fun `test part1`() {
        assertEquals(19114, Day19(sample).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(167409079868000L, Day19(sample).part2())
    }
}
