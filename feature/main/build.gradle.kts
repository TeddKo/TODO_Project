plugins {
    alias(libs.plugins.todo.android.library)
    alias(libs.plugins.todo.android.compose)
    alias(libs.plugins.todo.android.hilt)
}

android {
    namespace = "com.tedd.todo_project.feature.main"
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:domain"))
    implementation(project(":core:navigation"))
    implementation(project(":feature:histories"))
    

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.collections.immutable)
}
