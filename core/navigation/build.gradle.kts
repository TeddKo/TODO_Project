plugins {
    alias(libs.plugins.todo.android.library)
    alias(libs.plugins.todo.kotlin.serialization)
    alias(libs.plugins.todo.android.hilt)
    alias(libs.plugins.todo.android.compose)
}

android {
    namespace = "com.tedd.todo_project.core.navigation"
}

dependencies {
    implementation(libs.androidx.navigation.compose)
    implementation(libs.hilt.navigation.compose)
}