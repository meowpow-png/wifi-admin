@file:Suppress("UnstableApiUsage")

import hr.ht.rnd.wifiadmin.env.api.Environment
import hr.ht.rnd.wifiadmin.env.api.EnvironmentExtension
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.kotlin.dsl.the

buildscript {
    dependencies {
        classpath(libs.postgresql)
        classpath(libs.flyway.database.postgresql)
    }
}

plugins {
    java
    id("jvm-test-suite")
    id("java-test-fixtures")
    id("org.springframework.boot") version "4.1.0"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.flywaydb.flyway") version "12.10.0"
    id("hr.ht.rnd.wifiadmin.environment")
}

group = "hr.ht.rnd"
version = "0.0.1-SNAPSHOT"
description = "wifi-admin-backend"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

sourceSets {
    main {
        java {
            srcDir(layout.buildDirectory.dir("generated/sources/wsdl"))
        }
    }
}
val env: Environment = the<EnvironmentExtension>().environment

tasks.bootRun {
    environment(env.toMap())
    systemProperty("spring.profiles.active", "dev")
}

flyway {
    url = "jdbc:postgresql://localhost:${env.dbPort()}/${env.dbName()}"
    user = env.dbUser()
    password = env.dbPassword()
    cleanDisabled = false
}

testing {
    suites {
        withType<JvmTestSuite> {
            useJUnitJupiter()
        }
        register<JvmTestSuite>("integrationTest") {
            dependencies {
                implementation(project())
                implementation(testFixtures(project()))
                implementation(libs.spring.boot.starter.test)

                implementation(platform(libs.mockito.bom))
                implementation(libs.mockito.junit.jupiter)

                implementation(libs.okhttp3.mockwebserver)

                implementation(platform(libs.testcontainers.bom))
                implementation(libs.testcontainers.postgres)
                implementation(libs.testcontainers.jdbc)
            }
        }
        register<JvmTestSuite>("architectureTest") {
            dependencies {
                implementation(project())
                implementation(libs.tngtech.archunit)
                implementation(libs.tngtech.archunit.junit5)

                implementation(libs.spring.boot.starter.web)
            }
        }
    }
}

repositories {
    mavenCentral()
}

val cxfCodegen = configurations.create("cxfCodegen")

dependencies {
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.cxf.spring.boot.starter.jaxws)
    implementation(libs.springdoc.openapi.starter.webmvc.ui)
    implementation(libs.spring.boot.starter.flyway)
    implementation(libs.spring.boot.starter.actuator)

    runtimeOnly(libs.flyway.postgresql)
    runtimeOnly(libs.postgresql)

    cxfCodegen(libs.cxf.tools.wsdlto.core)
    cxfCodegen(libs.cxf.tools.wsdlto.frontend.jaxws)
    cxfCodegen(libs.cxf.tools.wsdlto.databinding.jaxb)
    cxfCodegen(libs.slf4j.simple)

    implementation(platform(libs.jjwt.bom))
    implementation(libs.jjwt.api)
    runtimeOnly(libs.jjwt.impl)
    runtimeOnly(libs.jjwt.jackson)

    testImplementation(libs.spring.boot.starter.test)
    testFixturesImplementation(platform(libs.jjwt.bom))
    testFixturesImplementation(libs.jjwt.api)
    testFixturesImplementation(libs.jjwt.impl)
}

tasks.register<JavaExec>("wsdl2java") {
    group = "soap"
    description = "Generates SOAP client classes from WSDL."

    classpath = cxfCodegen
    mainClass.set("org.apache.cxf.tools.wsdlto.WSDLToJava")

    args(
        "-d",
        layout.buildDirectory.dir("generated/sources/wsdl").get().asFile.absolutePath,
        "-p",
        "hr.ht.rnd.wifiadmin.infra.transport.soap.wsdl",
        "-wsdlLocation",
        "classpath:wsdl/wifi-platform.wsdl",
        "${projectDir}/src/main/resources/wsdl/wifi-platform.wsdl"
    )
}

tasks.compileJava {
    dependsOn(tasks.named("wsdl2java"))
}

tasks.withType<Test>().configureEach {
    outputs.upToDateWhen { false }
    useJUnitPlatform()
    testLogging {
        events(
            TestLogEvent.PASSED,
            TestLogEvent.SKIPPED,
            TestLogEvent.FAILED,
        )
        exceptionFormat = TestExceptionFormat.FULL
        showCauses = true
    }
}
