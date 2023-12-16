package mri.advent.y2023.utils

val VOID_LONG_RANGE = (0L until 0L)

fun LongRange.slide(delta: Long) = LongRange(this.first + delta, this.last + delta)

/**
 * Test if the given range intersects with current range: at least one common
 */
fun LongRange.intersects(other: LongRange) = !other.isEmpty() && ( this.includes(other) || this.first in other || this.last in other)

/**
 * Test if current range includes the given range: all elements of given range are part of current range.
 */
fun LongRange.includes(other: LongRange) = this.first <= other.first && this.last >= other.last

/**
 * Returns intersection between this range and the given one. (inclusive)
 * => return new range which contains all elements common to this and other ranges
 */
fun LongRange.intersection(other: LongRange): LongRange {
    if (!this.intersects(other))
        return VOID_LONG_RANGE
    return LongRange(maxOf(this.first, other.first), minOf(this.last, other.last))
}


fun LongRange.cut(cutRange: LongRange): List<LongRange> {
    if (!this.intersects(cutRange)) return listOf(this)
    if (cutRange.includes(this)) return emptyList()
    val splited = mutableListOf<LongRange>()
    // left part
    if (this.first < cutRange.first) splited.add(LongRange(this.first, cutRange.first - 1))
    // right part
    if (this.last > cutRange.last) splited.add(LongRange(cutRange.last + 1, this.last))
    return splited
}