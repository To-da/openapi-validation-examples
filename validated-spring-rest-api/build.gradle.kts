plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("ch.qos.logback:logback-classic")
    implementation("io.github.microutils:kotlin-logging:1.7.8")
    implementation("org.springdoc:springdoc-openapi-webmvc-core:1.2.30")
    implementation("org.springdoc:springdoc-openapi-kotlin:1.2.30")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("net.javacrumbs.json-unit:json-unit-spring:2.14.0")
    testImplementation("io.strikt:strikt-core:0.23.7")
}

