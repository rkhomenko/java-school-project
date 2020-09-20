plugins {
    id("java-library")
}

group = "org.khomenko.project.core"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(group = "com.google.guava", name = "guava")
    implementation(group = "com.fasterxml.jackson.core", name = "jackson-databind", version = "2.11.2")
    implementation(group = "org.springframework", name = "spring-context", version = "5.2.9.RELEASE")
    implementation(group = "org.springframework.data", name = "spring-data-jpa", version = "2.3.4.RELEASE")
    compileOnly(group = "javax.persistence", name = "javax.persistence-api", version = "2.2")
    compileOnly(group = "org.projectlombok", name = "lombok", version = "1.18.12")
    annotationProcessor(group = "org.projectlombok", name = "lombok", version = "1.18.12")
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
