plugins {
    alias(libs.plugins.todo.android.feature)
}

android {
    namespace = "com.tedd.todo_project.feature.main"
}

dependencies {
    implementation(project(":feature:histories"))

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.reorderable)
}