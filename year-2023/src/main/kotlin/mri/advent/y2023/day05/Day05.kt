package mri.advent.y2023.day05

import mri.advent.y2023.BaseDay
import mri.advent.y2023.utils.*

// Data model ----------------------------------------------------------------

data class ConversionRule(val sourceRange: LongRange, val delta: Long) {
    fun convert(source: Long): Long? = if (source in sourceRange) source + delta else null
}

data class ConversionMap(val name: String, val conversionRules: List<ConversionRule>) {
    // process a single source element
    fun convert(source: Long) = conversionRules.firstNotNullOfOrNull { it.convert(source) } ?: source

    // process a list of element ranges ( part 2 )
    fun convert(inputRanges: List<LongRange>): List<LongRange> {
        val outputRanges = mutableListOf<LongRange>()
        var sourceRanges = inputRanges.sortedBy { it.first }.toMutableList()
        debug("  processing conversion $name on $sourceRanges")
        this.conversionRules.forEach { rule ->
            debug("     apply conversion rule ${rule.sourceRange}")
            val unmappedRanges = mutableListOf<LongRange>()
            sourceRanges.forEach { sourceRange ->
                val intersection = sourceRange.intersection(rule.sourceRange)
                if (intersection.isEmpty()) {
                    unmappedRanges.add(sourceRange)
                } else {
                    outputRanges.add(intersection.slide(rule.delta))
                    unmappedRanges.addAll(sourceRange.cut(rule.sourceRange))
                }
            }
            sourceRanges = unmappedRanges
        }
        outputRanges.addAll(sourceRanges)
        return outputRanges
    }
}

data class Almanac(val seeds: List<Long>, val conversionMaps: List<ConversionMap>) {

    fun findMinimumLocationPart1() = seeds.minOf { seedToLocation(it) }
    fun findMinimumLocationPart2(): Long {
        val seedRanges = seeds.chunked(2).map { LongRange(it.get(0), it.get(0) + it.get(1) - 1) }

        debug("Found ${seedRanges.size} seed ranges to process: ${seedRanges.joinToString { "${it.first}-${it.last}" }}")
        var rangesToConvert = seedRanges.map { LongRange(it.first, it.last) }
        conversionMaps.forEach {
            rangesToConvert = it.convert(rangesToConvert)
        }
        return rangesToConvert.map { it.first }.min()
    }

    fun findMinimumLocation(seedsRange: LongRange): Long {
        var minimumFound = Long.MAX_VALUE
        seedsRange.forEach {
            val location = seedToLocation(it)
            if (location < minimumFound) {
                debug(" ${seedsRange.first} - found min: $location")
                minimumFound = location
            }
            if (it % 1000000 == 0L) debug(" range ${seedsRange.first}  - processed 1000000 seeds (iteration #${it / 1000000})")
        }
        return minimumFound
    }

    // find location for a fiven seed
    fun seedToLocation(seed: Long): Long {
        var current = seed
        this.conversionMaps.forEach { conversionMap ->
            current = conversionMap.convert(current)
        }
        return current
    }
}

// Input parsing functions -----------------------------------------------
fun String.toConversionRule() = with(this.toLongs()) { ConversionRule(LongRange(this[1], this[1] + this[2] - 1), this[0] - this[1]) }
fun parseConverionMap(lines: List<String>): ConversionMap {
    val name = lines.first().split(" ").first()
    return ConversionMap(name, lines.drop(1).map { it.toConversionRule() })
}

fun parseAlmanac(input: String) = with(input.asChunks()) {
    Almanac(
        this.first().first().substringAfter(": ").toLongs(),
        this.drop(1).map { parseConverionMap(it) }
    )
}

// main -------------------------------------------------------------------

class Day05(inFile: String = "/day05.in") : BaseDay(inFile) {
    // Part 1: 226 172 555   => time taken: 13 ms
    override fun part1() = parseAlmanac(data).findMinimumLocationPart1()

    // Part 2: 47 909 639   => time taken: 505 827 ms
    override fun part2() = parseAlmanac(data).findMinimumLocationPart2()
}

fun main() {
    Day05().run()
}

val DEBUG = false
fun debug(message: String) {
    if (DEBUG) println(message)
}