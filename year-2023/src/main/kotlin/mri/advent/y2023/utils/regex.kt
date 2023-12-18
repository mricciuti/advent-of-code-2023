package mri.advent.y2023.utils

/**
 * Try to extract groups from the given input using given regex.
 * throw exception if input does not match pattern or does not contain expected groups size
 */
fun String.extractGroups(regex: Regex, expectedGroups: Int): List<String> {
    val groups = regex.matchEntire(this)?.destructured?.toList()
        ?: throw IllegalArgumentException("$this does not match regex")
    if (groups.size != expectedGroups) {
        throw IllegalArgumentException("$this does not contain expected groups")
    }
    return groups
}