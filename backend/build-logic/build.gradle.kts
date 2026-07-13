plugins {
    `java-gradle-plugin`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("environment") {
            id = "hr.ht.rnd.wifiadmin.environment"
            implementationClass = "hr.ht.rnd.wifiadmin.EnvironmentPlugin"
        }
    }
}

dependencies {
    implementation("org.jspecify:jspecify:1.0.0")
}
