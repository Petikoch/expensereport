import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.jpa") version "1.6.10"
    jacoco
    id("info.solidsoft.pitest") version "1.7.0"
}

group = "com.nelkinda.training"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation("org.assertj:assertj-core:3.22.0")
    testImplementation("org.mockito:mockito-core:4.3.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

jacoco {
    toolVersion = "0.8.7"
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
    configure<JacocoTaskExtension> {
        includes = listOf("com.soleilentertainment.*", "com.nelkinda.*")
    }
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        csv.required.set(true)
        html.required.set(true)
    }
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                minimum = "0.0".toBigDecimal()
            }
        }
    }
}

tasks.check {
    dependsOn(tasks.jacocoTestCoverageVerification)
}

configure<info.solidsoft.gradle.pitest.PitestPluginExtension> {
    avoidCallsTo.set(setOf("kotlin.jvm.internal"))
    targetClasses.set(setOf("com.nelkinda.training.*"))
    pitestVersion.set("1.7.3")
    junit5PluginVersion.set("0.15")
    timestampedReports.set(false)
    outputFormats.set(setOf("XML", "HTML"))
    mutators.set(setOf("DEFAULTS", "STRONGER", "CONSTRUCTOR_CALLS", "INLINE_CONSTS", "REMOVE_CONDITIONALS", "REMOVE_INCREMENTS"))
    mutationThreshold.set(100)
    coverageThreshold.set(97)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.wrapper {
    gradleVersion = "7.3.3"
    distributionType = Wrapper.DistributionType.ALL
}
