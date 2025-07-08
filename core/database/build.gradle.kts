plugins {
    alias(libs.plugins.todo.android.library)
    alias(libs.plugins.todo.android.room)
    alias(libs.plugins.todo.android.hilt)
}

android {
    namespace = "com.tedd.todo_project.core.database"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(libs.kotlinx.datetime)
}
