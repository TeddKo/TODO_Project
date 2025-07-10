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
        register("todoAndroidRoom") {
            id = "todo.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
        register("todoAndroidFeature") {
            id = "todo.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("todoJvmLibrary") {
            id = "todo.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
        register("todoJvmHilt") {
            id = "todo.jvm.hilt"
            implementationClass = "JvmHiltConventionPlugin"
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
