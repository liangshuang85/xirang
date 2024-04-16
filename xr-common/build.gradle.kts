plugins {
    alias(libs.plugins.lombok.plugin)
}

dependencies {
    implementation(libs.commons.io)
    implementation(libs.commons.lang3)
    implementation(libs.jackson.databind)
    implementation(libs.guava)
    implementation(libs.mapstruct)
    implementation(libs.mybatis.plus.core)
    implementation(libs.mybatis.plus.extension)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.springdoc.openapi.starter.common)
    implementation(libs.sugar.commons)
    implementation(libs.sugar.crud)
    implementation(libs.therapi.runtime.javadoc)

    compileOnly(libs.lombok)

    annotationProcessor(libs.lombok)
    annotationProcessor(libs.mapstruct.processor)
    annotationProcessor(libs.therapi.runtime.javadoc.scribe)
}

tasks.withType<JavaCompile> {
    options.compilerArgs = listOf(
            "-Amapstruct.suppressGeneratorTimestamp=true",
            "-Amapstruct.suppressGeneratorVersionInfoComment=true",
            "-Amapstruct.defaultComponentModel=spring",
            "-Amapstruct.defaultInjectionStrategy=constructor",
            "-Amapstruct.unmappedTargetPolicy=IGNORE"
    )
}
