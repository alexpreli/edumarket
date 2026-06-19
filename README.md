# EduMarket - E-Commerce Platform for Digital Educational Products

This project was developed for the **"Mobile Device Software Development" (DSDM)** course. The application is built natively for the Android operating system, leveraging the latest modern technologies from the ecosystem (Jetpack Compose, Room, and Retrofit).

## Key Features (According to Project Requirements)

*   **Full Authentication:** Secure Registration (Register) and Login systems for users.
*   **Local Database (Room):** Persistent storage for educational digital products, shopping cart data, and offline capabilities.
*   **External API Integration (HTTP/Retrofit):** Dynamic fetching of the educational resources catalog (courses, PDF guides, templates) via asynchronous HTTP requests and JSON deserialization.
*   **Modern & Reactive UI:** Built entirely with Jetpack Compose, ensuring fluid navigation between screens and an adaptive layout without overlapping elements.
*   **Local State Management (DataStore/SharedPreferences):** Local storage for user session tokens and system preferences.

## Tech Stack & Architecture

The application is fully optimized using a clean, modern, and stable architecture:

*   **IDE:** Android Studio Quail 1 (2026.1.1)
*   **Language:** Kotlin (v2.0.21)
*   **Minimum SDK:** API 24 (Android 7.0 Nougat)
*   **Build System:** Kotlin DSL + Android Gradle Plugin (AGP v8.13.0) + Gradle Wrapper (v8.13)
*   **Architecture:** MVVM (Model-View-ViewModel) with State Hoisting for clean UI logic separation.
*   **Asynchronous Processing:** Kotlin Coroutines
*   **Database Management:** Google Room + KSP (`2.0.21-1.0.28`) for stable automated code generation.
*   **Networking:** Retrofit 2.11.0 + OkHttp 4.12.0 Logging Interceptor
*   **Image Loading:** Coil Compose 2.7.0

