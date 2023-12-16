package mri.advent.y2023.day06

import mri.advent.y2023.BaseDay
import mri.advent.y2023.utils.toLongs

/** --- Day 06:  --- */
class Day06(inFile: String = "/day06.in") : BaseDay(inFile) {

    data class Race(val time: Long, val distance: Long) {
        fun waysToWin() = (1 until time).map { holdDuration -> holdDuration * (time - holdDuration) }.count { it > distance }
    }

    override fun part1(): Any {
        val (times, distances) = data.lines().take(2)
            .map { it.substringAfter(": ") }.map { it.toLongs() }
        val races = times.mapIndexed { idx, time -> Race(time, distances[idx]) }
        return races.map { it.waysToWin() }.reduce { acc, ways -> acc * ways }
    }

    override fun part2(): Any {
        val (time, distance) = data.lines().take(2)
            .map { it.substringAfter(": ") }.map { it.replace(" ", "").toLong() }
        return Race(time, distance).waysToWin()
    }
}

fun main() {
    Day06().run()
}