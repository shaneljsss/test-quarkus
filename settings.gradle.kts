pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "io.quarkus") {
                useModule("io.quarkus:gradle-application-plugin:${requested.version}")
            }
        }
    }
}

rootProject.name = "test-quarkus"
