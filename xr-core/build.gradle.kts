plugins {
    alias(libs.plugins.lombok.plugin)
}

dependencies {
    implementation(project(":xr-common"))

    implementation(libs.aliyun.java.sdk.core) {
        exclude(group = "commons-logging", module = "commons-logging")
    }
    implementation(libs.aliyun.sms)
    implementation(libs.aws.apache.client) {
        exclude(group = "commons-logging", module = "commons-logging")
    }
    implementation(libs.aws.s3) {
        exclude(group = "software.amazon.awssdk", module = "netty-nio-client")
        exclude(group = "software.amazon.awssdk", module = "apache-client")
    }
    implementation(libs.caffeine)
    implementation(libs.commons.collections4)
    implementation(libs.commons.lang3)
    implementation(libs.commons.io)
    implementation(libs.lark.oapi.sdk) {
        exclude(group = "commons-logging", module = "commons-logging")
    }
    implementation(libs.mybatis.plus.spring.boot3.starter)
    implementation(libs.spring.boot.starter.aop)
    implementation(libs.spring.boot.starter.data.redis)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.session.data.redis)
    implementation(libs.sugar.commons)
    implementation(libs.sugar.crud)

    compileOnly(libs.jsr305)
    compileOnly(libs.lombok)

    runtimeOnly(libs.mysql.connector.j)

    annotationProcessor(libs.lombok)
}
