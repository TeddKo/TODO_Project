plugins {
    alias(libs.plugins.todo.android.feature)
}

android {
    namespace = "com.tedd.todo_project.feature.main"
}


dependencies {
    implementation(project(":feature:histories"))
    implementation(libs.androidx.core.splashscreen)
}
