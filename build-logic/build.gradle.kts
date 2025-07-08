plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

group = "com.tedd.todo_project.buildlogic"
version = "1.0.0"

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(libs.gradle.plugin.android)
    implementation(libs.gradle.plugin.kotlin)
    implementation(libs.gradle.plugin.room)
    compileOnly(libs.gradle.plugin.compose)
    compileOnly(libs.gradle.plugin.serialization)
    implementation(libs.gradle.plugin.ksp)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "todo.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "todo.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidHilt") {
            id = "todo.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("androidRoom") {
            id = "todo.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
        register("androidCompose") {
            id = "todo.android.compose"
            implementationClass = "AndroidComposeConventionPlugin"
        }
        register("kotlinSerialization") {
            id = "todo.kotlin.serialization"
            implementationClass = "KotlinSerializationConventionPlugin"
        }
    }
}
