plugins {
    alias(libs.plugins.todo.android.library)
    alias(libs.plugins.todo.android.hilt)
}

android {
    namespace = "com.tedd.todo_project.core.data"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:database"))

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.datetime)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
}
