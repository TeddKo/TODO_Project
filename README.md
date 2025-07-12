# TODO Project

A simple and powerful To-Do list application built with a modern Android tech stack, focusing on Clean Architecture and a modularized structure.

## ✨ Features

-   **Task Management**
    -   View and add new tasks.
    -   Swipe to delete a task.
    -   Drag and drop to reorder tasks.
    -   Mark tasks as complete.
-   **Selection Mode**
    -   Select and delete multiple tasks at once.
    -   "Select All" and "Deselect All" functionality.
-   **Completion History**
    -   View a list of completed tasks on a separate screen.
    -   Displays registration and completion dates for each task.
-   **Security**
    -   Task content is encrypted (AES) before being stored.
    -   The entire database is encrypted using SQLCipher.

---

## 🏗️ Structure

This project adopts a multi-module approach to separate features and responsibilities, promoting scalability and maintainability.

-   **`app`**: The main application module that integrates all other modules to build the final APK.
-   **`feature`**: A group of modules, each representing a specific app feature.
    -   `feature:main`: Contains the UI and logic for the main to-do list screen.
    -   `feature:histories`: Contains the UI and logic for the completed tasks history screen.
-   **`core`**: A group of library modules that provide shared functionality across feature modules.
    -   `core:data`: Manages data sources and repository implementations.
    -   `core:domain`: Contains pure business logic, including UseCases and domain models.
    -   `core:database`: Manages Room DB setup, DAOs, and Entities.
    -   `core:navigation`: Manages navigation logic for screen transitions.
    -   `core:ui`: Contains common Composable UI components.
    -   `core:designsystem`: Defines the app's design system, including colors, typography, and themes.
    -   `core:security`: Handles data encryption logic.
-   **`build-logic`**: Manages Convention Plugins to standardize Gradle configurations across modules.

---

## 🏛️ Architecture

-   **Clean Architecture**
    -   The project follows the principles of Clean Architecture, separating concerns into three main layers: **Presentation (feature) - Domain - Data**. This ensures a decoupled, testable, and maintainable codebase.
-   **MVI (Model-View-Intent)**
    -   The Presentation Layer is implemented using the MVI pattern.
    -   **Model**: The `ViewModel` manages a single, immutable state object (e.g., `MainScreenState`) exposed via a `StateFlow`.
    -   **View**: The Composable UI observes the `StateFlow` and renders the screen accordingly.
    -   **Intent**: User actions are modeled as explicit `Intent`s (e.g., `MainScreenIntent`), which are processed by the `ViewModel` to produce a new state. This creates a predictable, unidirectional data flow.
-   **Key Technologies**
    -   **UI**: 100% Jetpack Compose
    -   **DI (Dependency Injection)**: Hilt
    -   **Database**: Room, SQLCipher
    -   **Asynchronous**: Coroutines, Flow
    -   **Navigation**: Navigation for Compose
    -   **Build**: Gradle, Version Catalogs, Convention Plugins