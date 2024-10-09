import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.0.10"
    kotlin("plugin.spring") version "2.0.10"

    id("application")
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "com.github.nenadjakic.mailsender"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("org.modelmapper:modelmapper:3.2.1")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
        languageVersion.set(KotlinVersion.KOTLIN_2_1)
    }
}

tasks.bootJar {
    archiveFileName.set("${archiveBaseName.get()}.${archiveExtension.get()}")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}