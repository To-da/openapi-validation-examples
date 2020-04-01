import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
//  id("org.springframework.boot") version "2.2.6.RELEASE"
//  id("io.spring.dependency-management") version "1.0.9.RELEASE"
//    kotlin("jvm") version "1.3.71"
//  kotlin("plugin.spring") version "1.3.71"
    base
    kotlin("jvm") version "1.3.71" apply false
    kotlin("plugin.spring") version "1.3.71" apply false
}

allprojects {
    group = "com.toda.openapi"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
    }
}

subprojects {
//  java.sourceCompatibility = JavaVersion.VERSION_11


    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "11"
        }
    }
}
