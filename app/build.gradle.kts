plugins {
    id("com.android.application") version "4.2.0"
    kotlin("android") version "1.5.30"
    kotlin("kapt") version "1.5.30"
    kotlin("plugin.parcelize") version "1.5.30"
    kotlin("plugin.serialization") version "1.5.30"
//    id("com.google.gms.google-services") version "4.3.10"
//    id("com.google.firebase.crashlytics") version "2.7.1"

    /*
    !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        UNCOMMENT THIS and build will succeed.
        The firebase perf library attempts to modify/read bytecode and does not understand the Java 6 bytecode in Jetty causing the build errors.
    !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     */
    id("com.google.firebase.firebase-perf") version "1.4.0"

//    id("org.jlleitschuh.gradle.ktlint") version "10.0.0"
//    id("com.thunderhead.android.orchestration-plugin") version "5.0.1"
}

android {
    compileSdkVersion(30)
    defaultConfig {
        applicationId("com.jetty.example")
        minSdkVersion(24)
        targetSdkVersion(30)
        versionCode(1)
        versionName("1")
        testInstrumentationRunner("androidx.test.runner.AndroidJUnitRunner")
        // The following argument makes the Android Test Orchestrator run its
        // "pm clear" command after each test invocation. This command ensures
        // that the app's state is completely cleared between tests.
//        testInstrumentationRunnerArguments(mapOf("clearPackageData" to "true"))

        multiDexEnabled = true

        // Thunderhead One requirements
        renderscriptTargetApi = 22
        renderscriptSupportModeEnabled(true)
    }

    buildTypes {
        getByName("debug") {
            isDebuggable = true
            applicationIdSuffix = ".debug"
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")

            // Workaround for the firebaseCrashlytics { ... } block missing when using kotlin dsl
            // https://github.com/firebase/firebase-android-sdk/issues/2665#issuecomment-842771205
            // Also, this shouldn't even be needed as crashlytics should just ignore uploading the mapping if minify is not used
            // TODO check later!
//            the<com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension>().mappingFileUploadEnabled = false
        }
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
    }
    sourceSets {
        map { it.java.srcDir("src/${it.name}/kotlin") }
    }

//    lint {
//        disable("MissingTranslation")
//    }

    packagingOptions {
        resources.excludes.addAll(
            listOf(
                "META-INF/DEPENDENCIES.txt",
                "META-INF/LICENSE.txt",
                "META-INF/NOTICE.txt",
                "META-INF/NOTICE",
                "META-INF/LICENSE",
                "META-INF/DEPENDENCIES",
                "META-INF/notice.txt",
                "META-INF/license.txt",
                "META-INF/dependencies.txt",
                "META-INF/LGPL2.1",
                "META-INF/AL2.0"
            )
        )
    }

    // http://tools.android.com/tech-docs/unit-testing-support#TOC-Method-...-not-mocked.-
    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
        unitTests.apply {
            isReturnDefaultValues = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

/*
thunderhead {
    enabled.set(true) // default true
    resources {
        buildDirectory.set(File("$buildDir/orchestration/resources")) // default buildDir/orchestration/resources
    }
    classProcessing {
        logfile.set(File("$buildDir/orchestration/classProccessorLog.log")) // defaults to buildDir/orchestration/classProccessorLog.log
        javaSourceTarget.set(JavaVersion.VERSION_11) // defaults to 1.7
        javaOutputTarget.set(JavaVersion.VERSION_11)  // defaults to 1.7
        processMainConfiguration.set(true) // default true
        processTestConfiguration.set(true) // default true
        // Compilers can forget to add correct bytecode metadata which is required for the orchestration plugin.
        // Ignoring the metadata will allow the plugin to complete successfully if there is a missing metadata issue.
        ignoreMissingMetadata() // default do not ignore.
    }
}

thunderhead {
    enabled.set(true) // default true
    resources {
        buildDirectory.set(File("$buildDir/test")) // default buildDir/orchestration/resources
    }
    classProcessing {
        logfile.set(File("$buildDir/test/custom.log")) // defaults to buildDir/orchestration/classProccessorLog.log
        javaSourceTarget.set(JavaVersion.VERSION_1_8) // defaults to 1.7
        javaOutputTarget.set(JavaVersion.VERSION_1_8)  // defaults to 1.7
        processMainConfiguration.set(true) // default true
        processTestConfiguration.set(true) // default true
        // Compilers can forget to add correct bytecode metadata which is required for the orchestration plugin.
        // Ignoring the metadata will allow the plugin to complete successfully if there is a missing metadata issue.
        ignoreMissingMetadata() // default do not ignore.
    }
}*/

repositories {
    maven(url = "https://thunderhead.mycloudrepo.io/public/repositories/one-sdk-android")
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    val kotlinVersion = "1.5.30"
    // Kotlin Standard Library
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    // Kotlin Serialization Library (requires kotlin-stdlib >1.4.10) (make sure these are in sync)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")
    // Kotlin Coroutines Core
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    // Kotlin Coroutines play services support (Task/AsyncTask)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.5.2")

    // WorkManager (Job Scheduling)
    // TODO when we change targetsdk to 31, change to use work 2.7.0
    implementation("androidx.work:work-runtime-ktx:2.6.0")

    // AndroidX (Jetpack Libraries for Support/Display components)
    implementation("androidx.appcompat:appcompat:1.3.1")
    // implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.0")

    // AndroidX Fragment extensions
    val fragmentVersion = "1.3.6"
    implementation("androidx.fragment:fragment-ktx:$fragmentVersion")

    // AndroidX Preferences Library
    implementation("androidx.preference:preference-ktx:1.1.1")

    // AndroidX Lifecycle
    val lifecycleVersion = "2.3.1"
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-reactivestreams:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-common-java8:$lifecycleVersion")

    // AndroidX ViewPager2 (Carousel) Library
    implementation("androidx.viewpager2:viewpager2:1.0.0")

    // Firebase BoM for syncing Firebase library versions
    implementation(platform("com.google.firebase:firebase-bom:28.4.0"))
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-config-ktx")
    implementation("com.google.firebase:firebase-inappmessaging-display-ktx")
    implementation("com.google.firebase:firebase-perf-ktx")

    // Required after adding In-App Messaging to the app
    // https://stackoverflow.com/a/60492942 | https://github.com/firebase/firebase-android-sdk/issues/1320 (??? !!! xD)
    implementation("com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava")


//    implementation("com.google.android.gms:play-services-tagmanager:17.0.1")
//    implementation("com.google.android.gms:play-services-maps:17.0.1")
//    implementation("com.google.android.gms:play-services-location:18.0.0")
//    implementation("com.google.android.gms:play-services-auth:19.2.0")
//    implementation("com.google.android.gms:play-services-ads:20.3.0")

    // Google Material Design style components
    implementation("com.google.android.material:material:1.4.0")

    // Utilities for Maps SDK for Android (requires Google Play Services)
    implementation("com.google.maps.android:android-maps-utils:2.2.3")

    // https://github.com/google/flexbox-layout
    implementation("com.google.android.flexbox:flexbox:3.0.0")

    // Contentful API
    implementation("com.contentful.java:java-sdk:10.4.5")

    // SQL database
    val roomVersion = "2.3.0"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    testImplementation("androidx.room:room-testing:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    // RxJava2
    // TODO: RxJava3 is available, but might require some migration changes.
    implementation("io.reactivex.rxjava2:rxjava:2.2.21")
    // RxJava2 for Android
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
    // RxJava2 for Kotlin
    implementation("io.reactivex.rxjava2:rxkotlin:2.4.0")
    // RxJava2 based Android system permissions management
    // TODO: Version 0.12 and later supports RxJava3
    implementation("com.github.tbruyelle:rxpermissions:0.11")

    // Displaying dependency licences in TermsLicensesActivity
    implementation("de.psdev.licensesdialog:licensesdialog:2.1.0")

    // Markdown parsing
    // implementation("org.commonmark:commonmark:0.17.1")

    // Adds support for Java 8 Time/Date APIs for minSdkVersion <= 25
    implementation("com.jakewharton.threetenabp:threetenabp:1.3.0")

    // Freshchat integration (Our support chat service)
    implementation("com.github.freshdesk:freshchat-android:4.3.3")

    // Facebook login
    implementation("com.facebook.android:facebook-login:9.1.1")

    // Animations
    implementation("com.airbnb.android:lottie:3.7.0")

    // Kotlin native image loading and caching
    implementation("io.coil-kt:coil:1.2.1")
    implementation("io.coil-kt:coil-svg:1.2.1")

    // Retrofit (Http Client Library)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:adapter-rxjava:2.9.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")

    // Koin for Android - Dependency Injection framework
    implementation("io.insert-koin:koin-android:3.0.2")

    // Shimmer for Android (skeleton layout type of loading indicator)
    implementation("com.facebook.shimmer:shimmer:0.5.0")

    // PinView for Login
    implementation("io.github.chaosleung:pinview:1.4.4")

    // Navigation
    val navVersion = "2.3.5"
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")
    implementation("androidx.navigation:navigation-dynamic-features-fragment:$navVersion")
    androidTestImplementation("androidx.navigation:navigation-testing:$navVersion")

    // Custom tab
    implementation("androidx.browser:browser:1.3.0")

    // Markwon
    val markwonVersion = "4.6.2"
    implementation("io.noties.markwon:core:$markwonVersion")
    implementation("io.noties.markwon:html:$markwonVersion")
    implementation("io.noties.markwon:image-coil:$markwonVersion")

    // Thunderhead One
    // Analytics tool for different marketing related events and promotions
    implementation("com.thunderhead.android:one-sdk:10.0.2")


//    constraints {
//        implementation("org.mortbay.jetty:jetty-client:9.4.43.v20210629")
//        implementation("oauth.signpost:signpost-jetty6:2.1.1")
//    }
//    modules {
//        module("org.mortbay.jetty:jetty-client") {
//            replacedBy("org.eclipse.jetty:jetty-client")
//        }
//    }

    // This dependency still requires the deprecated jcenter()
    // https://github.com/KasperskyLab/Kaspresso/issues/228
    // https://github.com/KasperskyLab/Kaspresso
    androidTestImplementation("com.kaspersky.android-components:kaspresso:1.2.1")

    /**
     * Jitpack dependencies (https://jitpack.io/)
     */
    // https://github.com/PierfrancescoSoffritti/android-youtube-player
    implementation("com.github.PierfrancescoSoffritti.android-youtube-player:chromecast-sender:10.0.5")

    // https://github.com/pilgr/Paper
    implementation("com.github.pilgr:Paper:2.7.1")

    /** End of jitpack dependencies */

    /**
    TODO Picasso was used transitively via Firebase In-App messaging < v20.10
    but it was changed to use Glide in newer versions.

    Fix: Add Picasso separately and possibly refactor PicassoImageDownloader.kt later
     */
    implementation( "com.squareup.picasso:picasso:2.71828")

    // Testing Navigation
    androidTestImplementation("androidx.test:rules:1.4.0-alpha06")
    androidTestImplementation("androidx.test:runner:1.4.0-alpha06")
    androidTestUtil("androidx.test:orchestrator:1.4.0-alpha06")
    debugImplementation("androidx.fragment:fragment-testing:$fragmentVersion") {
        exclude("androidx.test", "core")
    }

    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.3.0")

    testImplementation("io.mockk:mockk:1.11.0")
    androidTestImplementation("io.mockk:mockk-android:1.11.0")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.4.3")
}

//configurations.all {
//    resolutionStrategy.dependencySubstitution {
//        substitute(module(""))
//    }
//}
//ktlint {
//    android.set(true)
//}

// Needed until upgraded to Kotlin 1.5
/*tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlinx.ExperimentalSerializationApi"
}*/
