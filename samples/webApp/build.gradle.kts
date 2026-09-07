import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsBrowserDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
}

fun KotlinJsBrowserDsl.configureWebpack() {
    val rootDirPath = project.rootDir.path
    val projectDirPath = project.projectDir.path
    commonWebpackConfig {
        outputFileName = "webApp.js"
        devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
            // Serve sources to debug inside browser
            static(rootDirPath)
            static(projectDirPath)
        }
    }
}

kotlin {
    js {
        outputModuleName.set("webApp")
        browser {
            configureWebpack()
        }
        binaries.executable()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        outputModuleName.set("webApp")
        browser {
            configureWebpack()
        }
        binaries.executable()
    }

    sourceSets.commonMain.dependencies {
        implementation(projects.samples.shared)
        implementation(libs.compose.ui)
    }
}
