import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.nio.file.Files

open class GenerateDay : DefaultTask() {

    @Input
    var day: String = "99"

    @TaskAction
    fun generate() {
        println(" generating source for day $day")
        val year = project.name.replace("year-", "")
        val srcDir = project.projectDir.toPath().resolve("src/main/kotlin/mri/advent/y$year")
        val testSrcDir = project.projectDir.toPath().resolve("src/test/kotlin/mri/advent/y$year")
        val dayTemplate = project.rootProject.projectDir.toPath().resolve("templates/DayDD.kt").toFile().readText()
        val testDayTemplate = project.rootProject.projectDir.toPath().resolve("templates/DayDDTest.kt").toFile().readText()

        val daySrcDir = srcDir.resolve("day$day")
        val dayTestSrcDir = testSrcDir.resolve("day$day")
        if (Files.exists(daySrcDir) || Files.exists(dayTestSrcDir)) {
            println("Day directories already exists - do nothing")
        } else {
            Files.createDirectories(daySrcDir)
            daySrcDir.resolve("day${day}.in").toFile().createNewFile()
            daySrcDir.resolve("Day${day}.kt").toFile().writeText(dayTemplate.replace("DD", day).replace("YYYY", year))
            Files.createDirectories(dayTestSrcDir)
            dayTestSrcDir.resolve("Day${day}Test.kt").toFile().writeText(testDayTemplate.replace("DD", day).replace("YYYY", year))
            dayTestSrcDir.resolve("day${day}_sample.in").toFile().createNewFile()
        }
    }
}