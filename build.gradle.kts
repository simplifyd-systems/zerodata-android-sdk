/*
 * Copyright (c) 2012-2019 Arne Schwabe
 * Distributed under the GNU GPL v2 with additional terms. For full terms see the file doc/LICENSE.txt
 */


buildscript {
    val kotlin_version by extra("1.3.61")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        jcenter()
        maven(url = "https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.5.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.30")
        classpath("com.google.gms:google-services:4.3.3")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.3.0")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.2.0")
        classpath("gradle.plugin.com.onesignal:onesignal-gradle-plugin:0.12.8")
        classpath("com.google.protobuf:protobuf-gradle-plugin:0.8.17")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }

}
