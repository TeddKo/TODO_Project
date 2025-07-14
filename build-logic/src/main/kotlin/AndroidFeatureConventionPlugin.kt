
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("todo.android.library")
                apply("todo.android.compose")
                apply("todo.android.hilt")
            }

            dependencies {
                add("implementation", project(":core:ui"))
                add("implementation", project(":core:domain"))
                add("implementation", project(":core:navigation"))

                add("implementation", findLibrary("hilt.navigation.compose"))
                add("implementation", findLibrary("androidx.lifecycle.runtime.compose"))
                add("implementation", findLibrary("androidx.lifecycle.viewmodel.ktx"))

                add("implementation", findLibrary("kotlinx.collections.immutable"))
                add("implementation", findLibrary("kotlinx.datetime"))

                add("implementation", platform(findLibrary("androidx.compose.bom")))
                add("implementation", findLibrary("androidx.ui"))
                add("implementation", findLibrary("androidx.ui.graphics"))
                add("implementation", findLibrary("androidx.ui.tooling.preview"))
                add("implementation", findLibrary("androidx.material3"))
                add("implementation", findLibrary("reorderable"))
            }
        }
    }
}
