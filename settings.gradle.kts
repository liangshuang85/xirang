dependencyResolutionManagement {
    repositories {
        maven {
            url = uri("https://mirror.lingycloud.com/repository/maven-releases/")
            content {
                includeGroup("org.sugarframework")
            }
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/central")
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/public")
        }
        mavenCentral()
    }
}

rootProject.name = "xirang-backend"
include("xr-common")
include("xr-core")
include("xr-rest")
