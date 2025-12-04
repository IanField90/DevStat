//@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
//    id("org.jetbrains.kotlin.android") version "2.1.10" apply false
}


android {
    compileSdk = 36
    buildToolsVersion = "36.1.0"

    namespace = "uk.co.ianfield.devstat"

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    defaultConfig {
        minSdk = 21
        targetSdk = 36
        applicationId = "uk.co.ianfield.devstat"
        versionCode = 26
        versionName = "2.4.8"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments.putAll(
            mapOf(
                "clearPackageData" to "true"
            )
        )

    }
    signingConfigs {
        create("release") {
            storeFile = file(extra["uk.co.ianfield.devstat.keystore.location"].toString())
            storePassword = extra["uk.co.ianfield.devstat.keystore.storepass"].toString()
            keyPassword = extra["uk.co.ianfield.devstat.keystore.keypass"].toString()
            keyAlias = extra["uk.co.ianfield.devstat.keystore.alias"].toString()
        }
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }

//    packagingOptions {
//        excludes.addAll(mutableSetOf("META-INF/services/javax.annotation.processing.Processor"))
//    }

    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_1_8
        }
    }

    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
        unitTests {
            isIncludeAndroidResources = true
        }
    }

//    sourceSets {
//        getByName("androidTest").resources.srcDirs("src/androidTest/res", "src/test/resources")
//    }

}

dependencies {
    implementation(libs.kotlin.stdlib.jdk8)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.recyclerview)
    implementation(libs.material)

    implementation(libs.androidx.core.ktx)

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.android.compiler)
    testImplementation(libs.hilt.android.testing)
    kaptTest(libs.hilt.android.compiler)

    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.hamcrest.library)
    testImplementation(libs.truth)
    testImplementation(libs.androidx.junit)

    androidTestImplementation(libs.junit)

    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.espresso.intents)
    androidTestImplementation(libs.androidx.annotation)
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.androidx.rules)
    androidTestImplementation(libs.androidx.junit)
    // Set this dependency if you want to use Hamcrest matching
    androidTestImplementation(libs.hamcrest.library)

    androidTestUtil(libs.androidx.orchestrator)

}

kapt {
    correctErrorTypes = true
}
