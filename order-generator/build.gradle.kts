plugins {
    id("org.springframework.boot") version "2.4.0-SNAPSHOT"
}

group = "org.khomenko.project.order.generator"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":core"))
    implementation(group = "com.google.guava", name = "guava")
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-data-jpa")
    implementation(group = "org.springframework.boot", name = "spring-boot-starter-web")
    compileOnly(group = "org.projectlombok", name = "lombok")
    annotationProcessor(group = "org.projectlombok", name = "lombok")
    testImplementation(group = "org.springframework.boot", name = "spring-boot-starter-test")
}

val test by tasks.getting(Test::class) {
    // Use junit platform for unit tests
    useJUnitPlatform()
}
