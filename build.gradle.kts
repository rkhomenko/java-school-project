plugins {
    id("io.spring.dependency-management")
    id("java")
}

subprojects {
    repositories {
        mavenCentral()
        maven { url = uri("https://repo.spring.io/milestone") }
        maven { url = uri("https://repo.spring.io/snapshot") }
    }

    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")

    dependencyManagement {
        val guavaVersion = "29.0-jre"
//        val junitVersion = "5.6.2"
        val javaFakerVersion = "1.0.2"

        dependencies {
            dependency("com.google.guava:guava:$guavaVersion")
//            dependencySet("org.junit.jupiter:$junitVersion") {
//                entry("junit-jupiter-api")
//                entry("junit-jupiter-engine")
//            }
            dependency("com.github.javafaker:javafaker:$javaFakerVersion")
        }
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
