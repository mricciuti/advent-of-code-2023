package mri.advent.y2023.utils.maths

// LCM - Least common multiple ( https://www.baeldung.com/kotlin/lcm )

fun leastCommonMultiple(a: Long, b: Long): Long {
    val larger = if (a > b) a else b
    val maxLcm = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % a == 0L && lcm % b == 0L) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}

fun List<Long>.leastCommonMultiple(): Long {
    var result = this[0]
    for (i in 1 until this.size) {
        result = leastCommonMultiple(result, this[i])
    }
    return result
}