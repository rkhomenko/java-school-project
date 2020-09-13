plugins {
    id("org.springframework.boot") version "2.4.0-SNAPSHOT"
    id("java-library")
}

group = "org.khomenko.project.core"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(group = "com.google.guava", name = "guava")
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-data-jpa")
    compileOnly(group = "org.projectlombok", name = "lombok")
    annotationProcessor(group = "org.projectlombok", name = "lombok")
    testImplementation(group = "org.springframework.boot", name = "spring-boot-starter-test")
}

val test by tasks.getting(Test::class) {
    // Use junit platform for unit tests
    useJUnitPlatform()
}

tasks {
    jar {
        enabled = true
    }
}
