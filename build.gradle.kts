plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.hilt.android) apply false
}

// Hilt 2.58's javac annotation processor bundles a kotlin-metadata-jvm that only
// understands Kotlin metadata up to 2.3.0; Kotlin 2.4.10 emits 2.4.0 metadata, which
// fails hiltJavaCompileDebug with "maximum supported version is 2.3.0". Force the
// version that matches this project's Kotlin version since kotlin-metadata-jvm has
// been unshaded (independently overridable) since Dagger 2.57.
allprojects {
    configurations.all {
        resolutionStrategy {
            force("org.jetbrains.kotlin:kotlin-metadata-jvm:${libs.versions.kotlin.get()}")
        }
    }
}
