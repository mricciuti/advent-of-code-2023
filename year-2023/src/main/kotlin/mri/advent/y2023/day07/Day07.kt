package mri.advent.y2023.day07

import mri.advent.y2023.BaseDay
import mri.advent.y2023.utils.compareTo

val cardsWithValues = "23456789TJQKA".mapIndexed { index, c -> c to if (c.isDigit()) c.digitToInt() else index + 2 }
const val JOKER = 'J'

data class Card(val type: Char) : Comparable<Card> {
    val value = cardsWithValues.first { it.first == type }.second
    val isJoker = this.type == JOKER
    override fun compareTo(other: Card) = this.value.compareTo(other.value)
}

enum class HandType(val cardsCountPerType: List<Int>) {
    HIGH(listOf(1, 1, 1, 1, 1)),
    ONE_PAIR(listOf(2, 1, 1, 1)),
    TWO_PAIRS(listOf(2, 2, 1)),
    THREE_OF_KIND(listOf(3, 1, 1)),
    FULL_HOUSE(listOf(3, 2)),
    FOUR_OF_KIND(listOf(4, 1)),
    FIVE_OF_KIND(listOf(5))
}

data class Hand(val cards: List<Card>, val bid: Int, private val useJokers: Boolean = false) : Comparable<Hand> {
    private val nbCardsPerType = cards.groupBy { it.type }.map { it.value.size }.sortedDescending()
    val handType =
        if (useJokers) getTypeWithJokers()
        else HandType.entries.first { it.cardsCountPerType == nbCardsPerType }

    // part 2 : can use jokers to get better hand value
    private fun getTypeWithJokers(): HandType {
        // cards per type without jokers
        val cardsCount = cards.filter { !it.isJoker }
            .groupBy { it.type }.map { it.value.size }.sortedDescending().toIntArray()
        // use jokers to increase best card found occurence
        cardsCount[0] += cards.count { it.isJoker }
        return HandType.entries.first { it.cardsCountPerType == cardsCount.toList() }
    }

    override fun compareTo(other: Hand): Int {
        if (this.handType == other.handType) {
            return this.cards.compareTo(other.cards)
        }
        return this.handType.compareTo(other.handType)
    }
}

// parse hand, ex.:  32T3K 765
fun String.toHand(part2: Boolean = false) = with(this.split(" ")) {
    Hand(this[0].map { Card(it) }.toList(), this[1].toInt(), useJokers = part2)
}

/** --- Day 07:  --- */
class Day07(inFile: String = "/day07.in") : BaseDay(inFile) {

    private fun getResult(part2: Boolean = false) = data.lines().map { it.toHand(part2) }
        .sorted() // sort hands by rank, ascending
        .mapIndexed { rank, hand -> hand.bid * (rank + 1) }
        .sum()

    override fun part1() = getResult()
    override fun part2() = getResult(part2 = true)
}

fun main() {
    Day07().run()
}