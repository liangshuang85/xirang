import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    alias(libs.plugins.gradle.git.properties.plugin)
    alias(libs.plugins.lombok.plugin)
    alias(libs.plugins.springframework.boot.plugin)
    alias(libs.plugins.spring.dependency.management.plugin)
}

dependencies {
    implementation(project(":xr-common"))
    implementation(project(":xr-core"))

    implementation(libs.caffeine)
    implementation(libs.flyway.mysql)
    implementation(libs.lettuce.core)
    implementation(libs.mybatis.plus.spring.boot3.starter)
    implementation(libs.p6spy.spring.boot.starter)
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.aop)
    implementation(libs.spring.boot.starter.cache)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.session.data.redis)
    implementation(libs.springdoc.openapi.starter.webmvc.ui)
    implementation(libs.sugar.commons)
    implementation(libs.sugar.crud)
    implementation(libs.sugar.preconfigure)
    implementation(libs.therapi.runtime.javadoc)
    implementation(libs.therapi.runtime.javadoc)

    compileOnly(libs.jsr305)
    compileOnly(libs.lombok)

    annotationProcessor(libs.lombok)
    annotationProcessor(libs.spring.boot.configuration.processor)
    annotationProcessor(libs.therapi.runtime.javadoc.scribe)
}

springBoot {
    buildInfo {
        properties {
            name.set("息壤")
        }
    }
}

gitProperties {
    keys = listOf("git.branch", "git.build.version", "git.commit.id", "git.commit.id.abbrev", "git.commit.time", "git.dirty", "git.tags")
    failOnNoGitDirectory = false
}

tasks.withType<BootJar> {
    launchScript()
}
