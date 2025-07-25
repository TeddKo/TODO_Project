plugins {
    alias(libs.plugins.todo.android.library)
    alias(libs.plugins.todo.android.compose)
}

android {
    namespace = "com.tedd.todo_project.core.ui"
}

dependencies {
    api(project(":core:designsystem"))
}
