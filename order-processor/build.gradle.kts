//plugins {
//    id("org.springframework.boot") version "2.4.0-SNAPSHOT"
//}

group = "org.khomenko.project.order.generator"
version = "1.0-SNAPSHOT"

configurations {
    implementation {
        exclude(group = "org.slf4j", module = "slf4j-log4j12")
    }
}

dependencies {
//    implementation(project(":core"))
    implementation(group = "com.google.guava", name = "guava")
    implementation(group = "com.fasterxml.jackson.core", name = "jackson-databind", version = "2.11.2")
    implementation(group = "org.slf4j", name = "slf4j-log4j12", version = "1.7.30")
    implementation(group = "org.springframework", name = "spring-context", version = "5.2.9.RELEASE")
    compileOnly(group = "org.apache.spark", name = "spark-core_2.12")
    compileOnly(group = "org.apache.spark", name = "spark-streaming_2.12")
    compileOnly(group = "org.apache.spark", name = "spark-streaming-kafka-0-10_2.12")
    compileOnly(group = "org.projectlombok", name = "lombok", version = "1.18.12")
    annotationProcessor(group = "org.projectlombok", name = "lombok", version = "1.18.12")
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = "org.khomenko.project.order.processor.main.Application"
    }

    from(configurations.runtimeClasspath.get().files
            .map { if (it.isDirectory) it else zipTree(it) })
}

val test by tasks.getting(Test::class) {
    // Use junit platform for unit tests
    useJUnitPlatform()
}
