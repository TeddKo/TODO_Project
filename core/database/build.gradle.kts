import org.gradle.api.attributes.Category
import org.gradle.api.attributes.LibraryElements
import org.gradle.api.attributes.Usage
import com.android.build.api.attributes.BuildTypeAttr

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
    implementation(project(":core:security"))
    implementation(libs.kotlinx.datetime)
    implementation(libs.sqlcipher)
    implementation(libs.sqlite.ktx)
}