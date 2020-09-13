pluginManagement {
    repositories {
        maven { url = uri("https://repo.spring.io/milestone") }
        maven { url = uri("https://repo.spring.io/snapshot") }
        //maven { url = uri("https://repo.spring.io/plugins-snapshot") }
        gradlePluginPortal()
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "org.springframework.boot") {
                useModule("org.springframework.boot:spring-boot-gradle-plugin:${requested.version}")
            }
        }
    }

    plugins {
        id("io.spring.dependency-management") version "1.0.10.RELEASE"
    }
}

include("order-generator")
