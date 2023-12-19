package mri.advent.y2023.day19

import mri.advent.y2023.BaseDay
import mri.advent.y2023.utils.asChunks
import mri.advent.y2023.utils.extractGroups

enum class Category { X, M, A, S }

const val PART_REGEX = """\{x=(\d*),m=(\d*),a=(\d*),s=(\d*)}"""

data class XmasPart(val ratings: Map<Category, Int>) {
    val score = ratings.values.sum()
}

fun String.parseXmasPart() = with(this.extractGroups(PART_REGEX.toRegex(), 4)) {
    XmasPart(this.mapIndexed { index, s -> Category.entries[index] to s.toInt() }.toMap())
}

enum class Operator(val code: Char) { GT('>'), LT('<') }
data class RuleCondition(val category: Category, val operator: Operator, val value: Int) {
    fun test(xmasPart: XmasPart): Boolean {
        val valToTest = xmasPart.ratings[category]!!
        return if (operator == Operator.GT) valToTest > value else valToTest < value
    }

    override fun toString() = "[ $category ${operator.code} $value ]"
}

fun String.parseRuleCondition() = with(extractGroups("""([xmas])([<>])(\d*)""".toRegex(), 3)) {
    val (cat, op, value) = this
    RuleCondition(Category.entries.first { it.name == cat.uppercase() }, Operator.entries.first { it.code == op[0] }, value.toInt())
}

sealed class Outcome(private val label: String) {
    class Accept : Outcome("ACCEPT")
    class Reject : Outcome("REJECT")
    class Move(val target: String) : Outcome("NEXT $target")

    override fun toString() = label
}

fun String.parseOutcome() = if (this == "A") Outcome.Accept() else if (this == "R") Outcome.Reject() else Outcome.Move(this)

data class Rule(val target: Outcome, val condition: RuleCondition? = null) {
    fun process(xmasPart: XmasPart): Outcome? {
        if (condition?.test(xmasPart) != false) return target
        return null
    }
}

fun String.parseRule() = with(this.split(":")) {
    if (this.size > 1) Rule(this[1].parseOutcome(), this[0].parseRuleCondition()) else Rule(this[0].parseOutcome())
}

data class Workflow(val name: String, val rules: List<Rule>) {
    fun process(xmasPart: XmasPart): Outcome {
        for (rule in rules) {
            rule.process(xmasPart)?.let { return it }
        }
        throw IllegalStateException(" no outcome found ")
    }
}

fun String.parseWorkflow() = with(extractGroups("""(\w*)\{(.*)}""".toRegex(), 2)) {
    val (name, rules) = this
    Workflow(name, rules.split(",").map { it.parseRule() })
}

data class Puzzle(val workflows: Map<String, Workflow>, val xmasParts: List<XmasPart>) {
    private val start = workflows["in"]!!

    private fun process(xmasPart: XmasPart): Outcome {
        debug("processing xmas $xmasPart")
        var currentWorkflow = start
        while (true) {
            debug("  wf $currentWorkflow")
            val outcome = currentWorkflow.process(xmasPart)
            debug("   => outcome: $outcome")
            if (outcome is Outcome.Accept || outcome is Outcome.Reject) return outcome
            currentWorkflow = workflows[(outcome as Outcome.Move).target]!!
        }
    }

    fun process() = xmasParts.map { it to process(it) }
}

class Day19(inFile: String = "/day19.in") : BaseDay(inFile) {

    private val puzzle = with(data.asChunks()) {
        Puzzle(this.first().map { it.parseWorkflow() }.associateBy { it.name }, this.last().map { it.parseXmasPart() })
    }

    override fun part1() = puzzle.process()
        .filter { it.second is Outcome.Accept }
        .sumOf { it.first.score }


    override fun part2(): Long {
        return 0L
    }

}

/*
Part 1: 402185
  => time taken: 28 ms

 */
fun main() {
    Day19().run()
}

const val DEBUG = false
fun debug(message: Any) {
    if (DEBUG) println(message)
}