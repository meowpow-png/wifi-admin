plugins {
    java
}

group = "hr.ht.rnd"
version = "0.0.1-SNAPSHOT"
description = "wifi-admin-backend"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}
