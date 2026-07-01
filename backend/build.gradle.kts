plugins {
    java
    id("org.springframework.boot") version "4.0.6"
    id("io.spring.dependency-management") version "1.1.7"
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

repositories {
    mavenCentral()
}

val cxfCodegen = configurations.create("cxfCodegen")

dependencies {
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.cxf.spring.boot.starter.jaxws)
    implementation(libs.springdoc.openapi.starter.webmvc.ui)

    cxfCodegen(libs.cxf.tools.wsdlto.core)
    cxfCodegen(libs.cxf.tools.wsdlto.frontend.jaxws)
    cxfCodegen(libs.cxf.tools.wsdlto.databinding.jaxb)
    cxfCodegen(libs.slf4j.simple)

    testImplementation(libs.spring.boot.starter.test)
}

tasks.register<JavaExec>("wsdl2java") {
    group = "soap"
    description = "Generates SOAP client classes from the WSDL."

    classpath = cxfCodegen
    mainClass.set("org.apache.cxf.tools.wsdlto.WSDLToJava")

    args(
        "-d",
        layout.buildDirectory.dir("generated/sources/wsdl").get().asFile.absolutePath,
        "-p",
        "hr.ht.rnd.wifiadmin.wifi.platform",
        "-wsdlLocation",
        "classpath:wsdl/wifi-platform.wsdl",
        "${projectDir}/src/main/resources/wsdl/wifi-platform.wsdl"
    )
}

tasks.compileJava {
    dependsOn(tasks.named("wsdl2java"))
}
