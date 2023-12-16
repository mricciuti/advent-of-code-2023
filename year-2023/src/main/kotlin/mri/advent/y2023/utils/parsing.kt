package mri.advent.y2023.utils

// utility functions

fun String.toInts(separator: String = " ") = this.split(separator).filter { it.isNotEmpty() }.map { it.toInt() }

fun String.toLongs(separator: String = " ") = this.split(separator).filter { it.isNotEmpty() }.map { it.toLong() }
fun String.toLongArray(separator: String = " ") = this.split(separator).filter { it.isNotEmpty() }.map { it.toLong() }.toLongArray()

fun String.asChunks(): List<List<String>> {
    val lineSep = if (this.contains("\r\n")) "\r\n" else "\n"
    return this
        .split("$lineSep$lineSep")
        .map {
            it.split(lineSep)
        }
}