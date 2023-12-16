package mri.advent.y2023.utils

// Compare two list of Comparable elements, by comparing each element sequentially.
fun <T : Comparable<T>> List<T>.compareTo(other: List<T>): Int  {
    assert(this.size == other.size)
    repeat(this.size) {
        val res = this[it].compareTo(other[it])
        if (res != 0) return res
    }
    return 0
}