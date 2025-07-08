
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.configure

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            dependencies {
                add("implementation", findLibrary("androidx.activity.compose"))
                add("implementation", platform(findLibrary("androidx.compose.bom")))
                add("implementation", findLibrary("androidx.material3"))
                add("implementation", findLibrary("androidx-material-icons"))
                add("implementation", findLibrary("androidx.ui.tooling.preview"))
                add("debugImplementation", findLibrary("androidx.ui.tooling"))
            }

            plugins.withId("com.android.application") {
                extensions.configure<ApplicationExtension> {
                    buildFeatures {
                        compose = true
                    }
                    composeOptions {
                        kotlinCompilerExtensionVersion = findVersion("composeCompiler")
                    }
                }
            }

            plugins.withId("com.android.library") {
                extensions.configure<LibraryExtension> {
                    buildFeatures {
                        compose = true
                    }
                    composeOptions {
                        kotlinCompilerExtensionVersion = findVersion("composeCompiler")
                    }
                }
            }
        }
    }
}
