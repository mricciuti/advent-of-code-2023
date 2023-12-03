package mri.advent.y2023.day02

import mri.advent.y2023.BaseDay

/** --- Day 02:  --- */
class Day02(inFile: String = "/day02.in") : BaseDay(inFile) {

    // 1 red, 2 green
    data class CubSet(val set: Map<String, Int>) {
        fun get(color: String) = set.getOrDefault(color, 0)
        fun isValid() = get("red") <= 12 && get("green") <= 13 && get("blue") <= 14
        fun power() = get("red") * get("green") * get("blue")
    }
    private fun String.toCubeSet() = CubSet(this.trim().split(", ").associate {
        val (number, color) = it.split(" ")
        color to number.toInt()
    })

    // Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
    data class Game(val id: Int, val cubeSets: List<CubSet>) {
        fun minimumSet() = CubSet(listOf("red", "blue", "green").associateWith { color -> cubeSets.maxOf { it.get(color) } })
    }
    private fun String.toGame(): Game {
        val parts = this.split(": ")
        val id = parts[0].split(" ")[1]
        return Game(id.toInt(), parts[1].split("; ").map { it.toCubeSet() })
    }

    override fun part1() = data.lines()
        .map { it.toGame() }
        .filter { it.cubeSets.all { it.isValid() } }
        .sumOf { it.id }

    override fun part2() = data.lines()
        .map { it.toGame() }
        .map { it.minimumSet() }
        .sumOf { it.power() }
}

fun main() {
    Day02().run()
}