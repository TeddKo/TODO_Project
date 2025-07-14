plugins {
    alias(libs.plugins.todo.android.library)
    alias(libs.plugins.todo.android.compose)
}

android {
    namespace = "com.tedd.todo_project.core.designsystem"
}

dependencies {
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.material)
}