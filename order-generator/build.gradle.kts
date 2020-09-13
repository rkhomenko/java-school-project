plugins {
    id("org.springframework.boot") version "2.4.0-SNAPSHOT"
}

dependencies {
    // This dependency is used by the application.
    implementation(group = "com.google.guava", name = "guava")

    // Use JUnit Jupiter API for testing.
    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-api")

    // Use JUnit Jupiter Engine for testing.
    testRuntimeOnly(group = "org.junit.jupiter", name = "junit-jupiter-engine")
}

val test by tasks.getting(Test::class) {
    // Use junit platform for unit tests
    useJUnitPlatform()
}
