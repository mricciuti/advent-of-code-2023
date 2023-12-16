package mri.advent.y2023.day12

import mri.advent.y2023.BaseDay
import mri.advent.y2023.utils.toInts

// Data model -------------------------------------------------------------
enum class SpringCondition(val code: Char) {
    OPERATIONAL('.'),   // operational
    DAMAGED('#'),       // damaged
    UNKNOWN('?');        // unknown status

    override fun toString() = this.code.toString()
}

fun Char.toCondition() = SpringCondition.entries.firstOrNull { it.code == this } ?: throw IllegalArgumentException("Unsupported: $this")

val knownStates = listOf(SpringCondition.DAMAGED, SpringCondition.OPERATIONAL)

data class SpringConditions(val conditions: List<SpringCondition>, val damagedGroups: List<Int>) {

    // check  if current springs setup matches damaged groups constraints
    fun currentGroupValid(): Boolean {
        assert(conditions.isNotEmpty() && conditions.first() == SpringCondition.DAMAGED)

        // no more groups expected
        if (damagedGroups.isEmpty()) {
            trace(" no more groups were expected")
            return false
        }
        // need enough conditions to satisfy group(s)
        val requiredConditions = damagedGroups.first() + if (damagedGroups.size > 1) 1 else 0
        if (conditions.size < requiredConditions) {
            trace(" not enough remaining conditions for groups")
            return false
        }
        // need separation before next group - e.g.  case  ##?# 3,x
        if (damagedGroups.size > 1 && conditions[damagedGroups.first()] == SpringCondition.DAMAGED) {
            trace("current group is too long , expect ${damagedGroups.size}")
            return false
        }
        // remaining damaged should not exceed expected amount
        val (remainingDamages, expectedtDamages) = conditions.count { it == SpringCondition.DAMAGED } to damagedGroups.sum()
        if (remainingDamages > expectedtDamages) {
            trace(" too much remaining damaged conditions $remainingDamages, expected=$expectedtDamages ")
            return false
        }
        // current group not satisfied
        if (conditions.take(damagedGroups.first()).any { it == SpringCondition.OPERATIONAL }) {
            trace(" current damaged group unsatisfied")
            return false
        }
        return true
    }

    fun possibleArrangements(cache: MutableMap<SpringConditions, Long> = mutableMapOf()): Long {

        // recurse stop conditions  -------------------------------------
        // 1. no more groups to process => no more Damaged expected
        if (damagedGroups.isEmpty()) {
            val remainingDamaged = conditions.count { it == SpringCondition.DAMAGED }
            trace(" all group processed : $remainingDamaged remaining damaged ")
            return if (remainingDamaged == 0) 1 // valid arrangement
            else 0 // invalid
        }
        // 2. no more spring to process => no more group expected
        if (conditions.isEmpty()) {
            trace(" all springs processed: ${damagedGroups.size} remaining groups")
            return if (damagedGroups.isEmpty()) 1 // valid
            else 0 // invalid
        }

        // part 2 : cache needed !!
        cache[this]?.let {
            trace(" found cache entry for $this")
            return it
        }

        var nbArrangements = 0L
        val currentCondition = conditions.first()

        // Case '?'  ==> test both cases,  and sum results
        if (currentCondition == SpringCondition.UNKNOWN) {
            knownStates.forEach { condition ->
                nbArrangements += replaceFirstWith(condition).possibleArrangements(cache)
            }
        }

        // Case '.'  => process next spring
        else if (currentCondition == SpringCondition.OPERATIONAL) {
            nbArrangements = SpringConditions(conditions.drop(1), damagedGroups).possibleArrangements(cache)
        }

        // Case '#"  => test current group validity & process next group
        else if (currentCondition == SpringCondition.DAMAGED) {
            if (!currentGroupValid()) {
                trace(" current config is invalid: $this")
                nbArrangements = 0
            } else {
                // process next group (if any)
                val nextGroupStart = if (damagedGroups.size == 1) damagedGroups.first() else damagedGroups.first() + 1
                nbArrangements = SpringConditions(conditions.drop(nextGroupStart), damagedGroups.drop(1)).possibleArrangements(cache)
            }
        }
        trace("found $nbArrangements arrangements for $this")
        cache[this] = nbArrangements
        return nbArrangements
    }

    private fun replaceFirstWith(condition: SpringCondition): SpringConditions {
        val others = this.conditions.drop(1)
        val updatedConditions = buildList {
            add(condition)
            addAll(others)
        }
        return SpringConditions(updatedConditions, this.damagedGroups)
    }
}

fun String.toSpringConditions() = with(this.split(" ")) {
    SpringConditions(this[0].map { it.toCondition() }, this[1].toInts(","))
}

fun String.repeat(times: Int, separator: String) = Array(times) { this }.joinToString(separator)
fun String.unfold() = with(this.split(" ")) {
    "${this[0].repeat(5, "?")} ${this[1].repeat(5, ",")}"
}

/** --- Day 12:  --- */
class Day12(inFile: String = "/day12.in") : BaseDay(inFile) {

    // 7047
    override fun part1() = data.lines()
        .map { it.toSpringConditions() }
        .sumOf { it.possibleArrangements() }

    // 17391848518844
    override fun part2() = data.lines()
        .map { it.unfold() }
        .map { it.toSpringConditions() }
        .parallelStream()
        .map { it.possibleArrangements() }
        .toList().sum()
}

fun main() {
    Day12().run()
}

const val TRACE = false
fun trace(message: Any) {
    if (TRACE) println(message)
}
