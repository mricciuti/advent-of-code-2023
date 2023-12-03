plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    add("implementation", kotlin("stdlib"))
    add("testImplementation", kotlin("test"))
    add("testRuntimeOnly", "org.junit.platform:junit-platform-launcher")
}

tasks.withType(Test::class) {
    useJUnitPlatform()
}

sourceSets {
    main {
        resources {
            srcDirs("src/main/kotlin")
            includes.add("**/*.in")
        }
    }
    test {
        resources {
            srcDirs("src/test/kotlin")
            includes.add("**/*.in")
        }
    }
}
// create day generation tasks
(1..25).map { it.toString().padStart(2, '0') }.forEach { day ->
    tasks.register("day-$day", GenerateDay::class.java) {
        group = "adventofcode"
        this.day = day
    }
}
