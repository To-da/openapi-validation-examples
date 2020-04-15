plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.3.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:1.3.3")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.0.2.RELEASE")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("ch.qos.logback:logback-classic")
    implementation("io.github.microutils:kotlin-logging:1.7.8")
    implementation("org.springdoc:springdoc-openapi-webflux-core:1.3.2")
    implementation("org.springdoc:springdoc-openapi-kotlin:1.3.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("net.javacrumbs.json-unit:json-unit-spring:2.14.0")
    testImplementation("io.strikt:strikt-core:0.23.7")
}

