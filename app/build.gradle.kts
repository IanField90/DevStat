plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}


android {
    compileSdk = 30
    buildToolsVersion = "30.0.3"

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    defaultConfig {
        minSdk = 14
        targetSdk = 30
        applicationId = "uk.co.ianfield.devstat"
        versionCode = 24
        versionName = "2.4.7"
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
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }

    packagingOptions {
        excludes.addAll(mutableSetOf("META-INF/services/javax.annotation.processing.Processor"))
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    sourceSets {
        getByName("androidTest").resources.srcDirs("src/androidTest/res", "src/test/resources")
    }
}

val hilt_version = "2.37"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.5.31")

    implementation("androidx.core:core-ktx:1.6.0")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("com.google.android.material:material:1.4.0")

    implementation("androidx.core:core-ktx:1.6.0")

    implementation("com.google.dagger:hilt-android:$hilt_version")
    kapt("com.google.dagger:hilt-android-compiler:$hilt_version")
    androidTestImplementation("com.google.dagger:hilt-android-testing:$hilt_version")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:$hilt_version")
    testImplementation("com.google.dagger:hilt-android-testing:$hilt_version")
    kaptTest("com.google.dagger:hilt-android-compiler:$hilt_version")

    testImplementation("junit:junit:4.12")
    testImplementation("org.mockito:mockito-core:3.10.0")
    testImplementation("org.hamcrest:hamcrest-library:2.2")
    testImplementation("com.google.truth:truth:1.1.2")
    testImplementation("androidx.test.ext:junit:1.1.3")

    androidTestImplementation("junit:junit:4.12")

    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.4.0")
    androidTestImplementation("androidx.annotation:annotation:1.2.0")
    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestImplementation("androidx.test:rules:1.4.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    // Set this dependency if you want to use Hamcrest matching
    androidTestImplementation("org.hamcrest:hamcrest-library:2.2")

    androidTestUtil("androidx.test:orchestrator:1.4.0")

}

kapt {
    correctErrorTypes = true
}
