# MagicPlayer

MagicPlayer is a modern media player application for Android, built with Jetpack Compose and other modern Android development tools.

## Features

*   **Modern UI:** The user interface is built entirely with Jetpack Compose, providing a declarative and modern UI framework.
*   **Dependency Injection:** Hilt is used for dependency injection to manage dependencies and improve modularity.
*   **Asynchronous Operations:** The app utilizes Kotlin Coroutines and WorkManager for handling background tasks and asynchronous operations efficiently.
*   **Local Data Storage:** Room is used for local data persistence, with the Paging library for handling large datasets.
*   **Networking:** Retrofit and OkHttp are used for making network requests to fetch media content or metadata.
*   **Image Loading:** Coil is used for loading and displaying images asynchronously.
*   **Media Playback:** The application includes media playback functionality, likely using the Jetpack Media3 library.
*   **WebView:** The Accompanist WebView library is used to display web content within the app.

## Tech Stack

*   [Kotlin](https://kotlinlang.org/)
*   [Jetpack Compose](https://developer.android.com/jetpack/compose)
*   [Hilt](https://dagger.dev/hilt/)
*   [Room](https://developer.android.com/training/data-storage/room)
*   [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)
*   [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager)
*   [Retrofit](https://square.github.io/retrofit/)
*   [OkHttp](https://square.github.io/okhttp/)
*   [Coil](https://coil-kt.github.io/coil/)
*   [Jetpack Media3](https://developer.android.com/jetpack/media3)
*   [Accompanist](https://google.github.io/accompanist/)

## Project Structure

The project is divided into two main modules:

*   `:app`: This is the main application module that contains the UI and application-level logic.
*   `:toolkit`: This module likely contains shared utilities, components, or other reusable code that is used by the `:app` module.

## Getting Started

To build and run the project, you will need Android Studio.

1.  Clone the repository: `git clone https://github.com/your-username/MagicPlayer.git`
2.  Open the project in Android Studio.
3.  Let Android Studio sync the project with Gradle.
4.  Run the `app` configuration on an Android emulator or a physical device.
