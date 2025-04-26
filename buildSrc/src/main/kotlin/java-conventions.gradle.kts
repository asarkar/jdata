import com.github.spotbugs.snom.SpotBugsTask

// https://docs.gradle.org/current/samples/sample_publishing_convention_plugins.html
// https://docs.gradle.org/current/userguide/version_catalogs.html
// Convention plugins in buildSrc do not automatically resolve external plugins.
// We must declare them as dependencies in buildSrc/build.gradle.kts.
// Apply the plugin manually as a workaround.
plugins {
    `java-library`
    // https://docs.gradle.org/current/userguide/pmd_plugin.html
    pmd
    // https://github.com/spotbugs/spotbugs-gradle-plugin
    id("com.github.spotbugs")
    // https://github.com/diffplug/spotless/tree/main/plugin-gradle
    id("com.diffplug.spotless")
    // https://github.com/tbroyer/gradle-errorprone-plugin
    id("net.ltgt.errorprone")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks.compileJava {
    options.compilerArgs.addAll(listOf("-Werror"))
}

repositories {
    mavenCentral()
}

// Access the version catalog.
val libs = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

// https://abhiappmobiledeveloper.medium.com/difference-between-implementation-api-compile-and-runtimeonly-in-gradle-dependency-55b70215d245
dependencies {
    compileOnly(platform(libs.findLibrary("junit-bom").get()))
    compileOnly(libs.findLibrary("spotbugs-annotations").get())
    errorprone(libs.findLibrary("errorprone").get())
    testImplementation(platform(libs.findLibrary("junit-bom").get()))
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation(libs.findLibrary("assertj-core").get())
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

pmd {
    // All versions here: https://docs.pmd-code.org/
    toolVersion = "${libs.findVersion("pmd").get()}"
    rulesMinimumPriority = 5
    ruleSetFiles = files("${rootDir}/config/pmd/pmd.xml")
}

spotbugs {
    toolVersion = "${libs.findVersion("spotbugs").get()}"
}

spotless {
    java {
        palantirJavaFormat("${libs.findVersion("palantirJavaFmt").get()}")
        toggleOffOn()
    }
}

val ci: Boolean by lazy { System.getenv("CI") != null }

tasks.named("spotlessCheck") {
    enabled = ci
}

tasks.named("check") {
    dependsOn("spotlessApply")
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        showStandardStreams = true
    }
}

tasks.withType<SpotBugsTask> {
    reports.create("html") {
        required = !ci
    }
}

tasks.withType<Pmd> {
    reports {
        xml.required = false
        html.required = !ci
    }
    isConsoleOutput = ci
}

tasks {
    javadoc {
        options {
            (this as CoreJavadocOptions).addBooleanOption("Xdoclint:none", true)
        }
    }
}
