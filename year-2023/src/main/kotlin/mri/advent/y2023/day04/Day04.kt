package mri.advent.y2023.day04

import mri.advent.y2023.BaseDay
import mri.advent.y2023.utils.toInts

// spec: Card $id : list(winningNumbers)  | list(myNumbers)
// ex.:  Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
data class Card(val winningNumbers: Set<Int>, val myNumbers: Set<Int>) {
    val matchingNumbers = myNumbers.count { winningNumbers.contains(it) }

    // The first match makes the card worth one point
    // and each match after the first doubles the point value of that card.
    fun score(): Int {
        if (matchingNumbers == 0) return 0
        var score = 1
        repeat(matchingNumbers - 1) { score *= 2 }
        return score
    }
}

fun String.toCard(): Card {
    val (winning, owned) = this.substringAfter(": ").split(" | ")
    return Card(winning.toInts().toSet(), owned.toInts().toSet())
}

class Day04(inFile: String = "/day04.in") : BaseDay(inFile) {

    override fun part1()  = data.lines().map { it.toCard() }.sumOf { it.score() }

    override fun part2(): Any {
        val cards = data.lines().map { it.toCard() }
        val finalCards = IntArray(cards.size) { 1 }
        cards.forEachIndexed { index, card ->
            repeat(card.matchingNumbers) {
                finalCards[index + it + 1] += finalCards[index]
            }
        }
        return finalCards.sum()
    }
}

fun main() {
    Day04().run()
}