plugins {
    alias(libs.plugins.todo.android.application)
    alias(libs.plugins.todo.android.hilt)
    alias(libs.plugins.todo.android.compose)
}

android {
    namespace = "com.tedd.todo_project"
}



dependencies {
    implementation(project(":feature:main"))
    implementation(project(":feature:histories"))
    implementation(project(":core:navigation"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:data"))
    implementation(project(":core:domain"))
    implementation(project(":core:database"))
    implementation(project(":core:security"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.core.splashscreen)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}