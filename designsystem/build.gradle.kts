plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.paparazzi)
}

android {
    namespace = "fr.leboncoin.designsystem"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
}

kotlin {
    jvmToolchain(17)
}

// Paparazzi 2.0.0-alpha04's classes are compiled for Java 21; only the test JVM needs bumping,
// not this module's own compile target (kept at 17 per the Phase A toolchain decision).
tasks.withType<Test> {
    javaLauncher = javaToolchains.launcherFor {
        languageVersion = JavaLanguageVersion.of(21)
    }
    // Paparazzi 2.0.0-alpha04's own HTML test reporter references a Gradle-internal class
    // (org.gradle.reporting.HtmlWriterTools) that doesn't exist in this project's Gradle 8.13,
    // crashing report generation after tests already passed. JUnit XML output (unaffected) is
    // what actually determines pass/fail, so disabling the HTML report sidesteps the bug.
    reports.html.required.set(false)
}

dependencies {
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    api(platform(libs.spark.bom))
    api(libs.spark)
    implementation(libs.coil.compose)

    testImplementation(libs.junit)
}
