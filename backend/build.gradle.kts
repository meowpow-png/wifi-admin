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
    id("jacoco")
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
                implementation(libs.spring.boot.starter.security)
                implementation(libs.spring.boot.starter.actuator)
                implementation(libs.spring.boot.jpa.test)
                implementation(libs.spring.boot.webmvc.test)
                implementation(libs.cxf.spring.boot.starter.jaxws)
                implementation(libs.springdoc.openapi.starter.webmvc.ui)

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
val wsdlPackage = "hr.ht.rnd.wifiadmin.infra.transport.soap.wsdl"
val wsdlPath = wsdlPackage.replace('.', '/')
val wsdlInputDir = layout.projectDirectory.dir("src/main/resources/wsdl")
val wsdlOutputDir = layout.buildDirectory.dir("generated/sources/wsdl")
val wsdlFile = layout.projectDirectory.file("src/main/resources/wsdl/wifi-platform.wsdl")

tasks.register<JavaExec>("wsdl2java") {
    group = "soap"
    description = "Generates SOAP client classes from WSDL."

    classpath = cxfCodegen
    mainClass.set("org.apache.cxf.tools.wsdlto.WSDLToJava")

    inputs.dir(wsdlInputDir)
    outputs.dir(wsdlOutputDir)

    args(
        "-d",
        wsdlOutputDir.get().asFile.absolutePath,
        "-p",
        wsdlPackage,
        "-wsdlLocation",
        "classpath:wsdl/wifi-platform.wsdl",
        wsdlFile.asFile.absolutePath
    )
}

tasks.register("compileAllClasses") {
    group = LifecycleBasePlugin.BUILD_GROUP
    description = "Compiles all project source sets."

    dependsOn(
        tasks.named("classes"),
        tasks.named("testClasses"),
        tasks.named("testFixturesClasses"),
        tasks.named("integrationTestClasses"),
        tasks.named("architectureTestClasses")
    )
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

tasks.register<JacocoReport>("coverage") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Generates an aggregate code coverage report."

    executionData(
        tasks.named<Test>("test").get(),
        tasks.named<Test>("integrationTest").get(),
    )
    sourceDirectories.setFrom(sourceSets.main.get().allSource.srcDirs)
    classDirectories.setFrom(
        files(
            sourceSets.main.get().output.asFileTree.matching {
                exclude("$wsdlPath/**")
            }
        )
    )
    reports {
        html.required.set(true)
        xml.required.set(true)
        csv.required.set(false)
    }
}

tasks.compileJava {
    dependsOn(tasks.named("wsdl2java"))
}
val test = tasks.named<Test>("test")
val integrationTest = tasks.named<Test>("integrationTest")
val coverage = tasks.named("coverage")

integrationTest {
    mustRunAfter(test)
}

coverage {
    mustRunAfter(test, integrationTest)
}

tasks.check {
    finalizedBy(integrationTest)
}
