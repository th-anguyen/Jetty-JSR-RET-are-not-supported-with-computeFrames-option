//dependencyResolutionManagement {
//    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
//    repositories {
//        google()
//        mavenCentral()
//        jcenter() // Warning: this repository is going to shut down soon
//    }
//}

rootProject.name = "HMA-Test"
include(":app")
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://thunderhead.mycloudrepo.io/public/repositories/one-sdk-android")
        maven(url = "https://repo.spring.io/milestone")
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id.startsWith("com.android")) {
                useModule("com.android.tools.build:gradle:${requested.version}")
            }
            if (requested.id.id.startsWith("org.jetbrains.kotlin")) {
                useVersion("${requested.version}")
            }
            // Temporary workaround till Google adds their plugin to the gradle repository
            if (requested.id.id.startsWith("com.google.gms.google-services")) {
                useModule("com.google.gms:google-services:${requested.version}")
            }
            if (requested.id.id.startsWith("com.google.firebase.crashlytics")) {
                useModule("com.google.firebase:firebase-crashlytics-gradle:${requested.version}")
            }
            if (requested.id.id.startsWith("com.google.firebase.firebase-perf")) {
                useModule("com.google.firebase:perf-plugin:${requested.version}")
            }
//            if (requested.id.id.startsWith("com.thunderhead.android.orchestration-plugin")) {
//                useModule("com.thunderhead.android:orchestration-plugin:${requested.version}")
//            }
        }
    }
}
