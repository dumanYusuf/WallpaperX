// Top-level build file where you can add configuration options common to all sub-projects/modules.



buildscript {
    repositories {
        // other repositories...
        mavenCentral()
        google() // Google reposunu eklediğinizden emin olun

    }
    dependencies {
        // other plugins...
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.52")

    }
}


plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("com.google.dagger.hilt.android") version "2.52" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false

}