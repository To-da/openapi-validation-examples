import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.2.6.RELEASE"
	id("io.spring.dependency-management") version "1.0.9.RELEASE"
	kotlin("jvm") 
	kotlin("plugin.spring") 
}

java.sourceCompatibility = JavaVersion.VERSION_11

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("com.github.kittinunf.fuel:fuel:2.2.1")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("ch.qos.logback:logback-classic")
	implementation("io.ktor:ktor-client-core:1.3.2")
	implementation("io.ktor:ktor-client-core-jvm:1.3.2")
	implementation("io.ktor:ktor-client-apache:1.3.2")
	implementation("io.ktor:ktor-client-logging-jvm:1.3.2")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
}

