plugins {
    alias(libs.plugins.todo.jvm.library)
    alias(libs.plugins.todo.jvm.hilt)
}

dependencies {
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.coroutines.core)
}
