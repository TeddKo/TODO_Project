plugins {
    alias(libs.plugins.todo.android.library)
    alias(libs.plugins.todo.android.compose)
    alias(libs.plugins.todo.android.hilt)
}

android {
    namespace = "com.tedd.todo_project.feature.histories"
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:domain"))
    implementation(project(":core:navigation"))

    implementation(libs.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.kotlinx.datetime)
}
