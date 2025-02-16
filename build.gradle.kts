plugins {
    val kotlinVersion = "1.9.25"
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"

    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("kapt") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
    idea
}

springBoot {
    buildInfo()
}

group = "com.musinsa"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}


dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.arrow-kt:arrow-fx-coroutines:1.1.3") // https://arrow-kt.io/learn/quickstart/

    implementation("com.querydsl:querydsl-jpa:5.1.0:jakarta")
    implementation("com.querydsl:querydsl-collections:5.1.0")
    implementation("com.querydsl:querydsl-sql:5.1.0")
    kapt("com.querydsl:querydsl-apt:5.1.0:jakarta")

    implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")

    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.3.0")

    runtimeOnly("com.h2database:h2")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
}

//idea {
//    module {
//        val kaptMain = file("build/generated/source/kapt/main")
//        sourceDirs.add(kaptMain)
//        generatedSourceDirs.add(kaptMain)
//    }
//}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
