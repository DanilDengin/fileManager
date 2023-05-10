// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.3.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
    }
}

plugins {
    id("io.gitlab.arturbosch.detekt").version("1.23.0-RC3")
    id("org.jlleitschuh.gradle.ktlint").version("11.3.2")
}

allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "io.gitlab.arturbosch.detekt")
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
