rootProject.name = "advent-of-code-2023"

listOf("year-2023", "year-2022", "year-2021").forEach {
    if (file(it).exists()) {
        include(it)
    }
}

