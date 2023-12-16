package mri.advent.y2023.day07

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * --- Day 07 Test:  ---
 */
class Day07Test {

    val sample = "/day07_sample.in"

    @Test
    fun `test part1`() {
        assertEquals(6440, Day07(sample).part1())
    }

    @Test
    fun `test part2`() {
        assertEquals(5905, Day07(sample).part2())
    }

    @Test
    fun testModels() {
        // Cards
        assertEquals(14, Card('A').value)
        assertEquals(10, Card('T').value)
        assertEquals(5, Card('5').value)

        // Hands
        with("32T3K 765".toHand()) {
            assertEquals(765, this.bid)
            assertEquals(3, this.cards.first().value)
            assertEquals(13, this.cards.last().value)
        }
        assertEquals(HandType.FIVE_OF_KIND, "AAAAA 1".toHand().handType)
        assertEquals(HandType.FOUR_OF_KIND, "AA8AA 1".toHand().handType)
        assertEquals(HandType.FULL_HOUSE, "23332 1".toHand().handType)
        assertEquals(HandType.THREE_OF_KIND, "TTT98 1".toHand().handType)
        assertEquals(HandType.TWO_PAIRS, "23432 1".toHand().handType)
        assertEquals(HandType.ONE_PAIR, "A23A4 1".toHand().handType)
        assertEquals(HandType.HIGH, "23456 1".toHand().handType)
    }
}
