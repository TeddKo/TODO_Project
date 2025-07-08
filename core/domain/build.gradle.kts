plugins {
    alias(libs.plugins.todo.android.library)
    alias(libs.plugins.todo.android.hilt)
}

android {
    namespace = "com.tedd.todo_project.core.domain"
}

dependencies {
    implementation(libs.kotlinx.datetime)
}
