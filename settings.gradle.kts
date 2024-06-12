pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "FancyMansionRenewal"
include(":app")
include(":core:common")
include(":core:presentation")
include(":domain:model")
include(":domain:interfaceController")
include(":domain:interfaceRepository")
include(":domain:usecase")
include(":data")
include(":di:injectController")
include(":di:injectRepository")
include(":presentation:viewer")
include(":test")
