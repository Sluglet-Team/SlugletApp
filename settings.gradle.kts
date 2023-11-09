pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            // tomtom
            url = uri("https://repositories.tomtom.com/artifactory/maven")
        }
    }
}

rootProject.name = "SlugletApp"
include(":app")
