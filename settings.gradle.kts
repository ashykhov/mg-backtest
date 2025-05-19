rootProject.name = "backtest"

// <@key=Composite build setup>start (generated)
// includeLibsIfExist(
//    "../../libs/commons",
// "../../libs/kediatRv2",
// "../../libs/kRecordable",
// "../../service-api/backtest-api",
// "../../service-api/depth-api",
// "../../service-api/engine-api",
// )
// <@key=Composite build setup>end (generated)

// Generated common part start

fun includeLibsIfExist(vararg paths: String) {
    val rootProjectDir = "../.."
    var hasIncludedLibs = false

    paths.forEach { path ->
        val libPath = file(path)
        if (libPath.exists()) {
            includeBuild(path)
            println("Including library from: $path")
            hasIncludedLibs = true
        } else {
            println("Library not found locally at: $path, will use published version")
        }
    }

    // Включаем кэш только если есть подключенные локальные библиотеки
//    if (hasIncludedLibs) {
//        buildCache {
//            local {
//                isEnabled = true
//                directory = File(rootProjectDir, "build-cache")
//            }
//        }
//        println("Build cache enabled at: $rootProjectDir/build-cache")
//    }
}

pluginManagement {
    repositories {
        mavenCentral()
        maven {
            url = uri("https://repo.repsy.io/mvn/money-glitch-dev/mg")
            credentials {
                username = System.getenv("REPSY_USERNAME")
                    ?: providers.gradleProperty("repoUsername").get()
                password = System.getenv("REPSY_TOKEN")
                    ?: providers.gradleProperty("repoUsername").get()
            }
        }
        gradlePluginPortal()
        mavenLocal()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "com.shykhov.mg.lib.central-plugin") {
                useModule("com.shykhov.mg.lib:central-plugin:${requested.version}")
            }
        }
    }
}
// Generated common part end
