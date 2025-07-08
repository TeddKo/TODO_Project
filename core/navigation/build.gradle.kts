plugins {
    alias(libs.plugins.todo.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.todo.kotlin.serialization)
    alias(libs.plugins.todo.android.hilt)
    alias(libs.plugins.todo.android.compose)
}

android {
    namespace = "com.tedd.todo_project.core.navigation"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.android.compiler)
}
