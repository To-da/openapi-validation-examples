import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.2.6.RELEASE" apply false
    id("io.spring.dependency-management") version "1.0.9.RELEASE" apply false
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
    apply(plugin = "kotlin")

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            /**
             * -Xjsr305=strict : to have null-safety taken in account in Kotlin types inferred from libraries APIs
             * -Xopt-in=kotlin.RequiresOptIn : experimental features enabled by default
             */
            freeCompilerArgs = listOf("-Xjsr305=strict","-Xopt-in=kotlin.RequiresOptIn")
            jvmTarget = JavaVersion.VERSION_11.majorVersion
        }
    }
}
