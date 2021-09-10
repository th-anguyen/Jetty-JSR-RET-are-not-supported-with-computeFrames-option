// Top-level build file where you can add configuration options common to all sub-projects/modules.
allprojects {
    repositories {
        mavenCentral()
        google()
        maven(url = "https://jitpack.io")
        //maven(url = "https://thunderhead.mycloudrepo.io/public/repositories/one-sdk-android")
        //maven(url = "https://repo.spring.io/milestone")
    }
}

tasks.create<Delete>("clean") {
    delete = setOf (rootProject.buildDir)
}

