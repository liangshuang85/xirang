plugins {
    java
    `java-library`
    `maven-publish`
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "java-library")

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    group = "eco.ywhc"
    version = rootProject.file("version.txt").readText().trim()

    configurations {
        compileOnly {
            extendsFrom(configurations.annotationProcessor.get())
        }
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }
}

tasks.jar {
    enabled = false
}

tasks.build {
    enabled = false
}
