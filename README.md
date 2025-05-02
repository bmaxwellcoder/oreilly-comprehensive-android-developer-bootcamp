# The Comprehensive Android Developer Bootcamp

This repository contains my coursework and projects from the O'Reilly Comprehensive Android Developer Bootcamp taught by instructor Paulo Dichone. Through this course, I've built multiple Android applications, each showcasing different aspects of Android development.

## Course Content

This repository contains various projects covering Android development concepts including:

- **Basic Android App Development** - Java fundamentals, layouts, and UI components
- **Architecture Patterns** - MVC implementation in Android
- **Activity Lifecycle** - Understanding Android's activity lifecycle
- **Networking** - Web connections and JSON parsing
- **Persistence** - Shared Preferences, SQL databases, and Room
- **UI Components** - ListViews, RecyclerViews, and custom widgets
- **Location Services** - Maps integration and geolocation
- **Multimedia** - Android media playback and recording
- **Animations** - View and property animations
- **Material Design** - Implementing Material Design principles
- **Fragments** - Managing UI components with fragments
- **Firebase** - Cloud database integration

## Project Notes
This repository is primarily for learning purposes. Many of the apps could 
benefit from UI enhancements and updated code to address deprecated 
features. However, similar to process of the learning and growth,
this repository remains a work in progress.

### MakeItRain
A simple app demonstrating UI interaction and basic Java programming.

### TrueCitizenQuiz
An MVC-based quiz application showing architectural patterns.

### ContactManager
An app showcasing database operations using SQLite.

### NoDo
A task management app using Android Room for persistence.

### EarthquakeApp
An app that displays earthquake information, featuring network operations and JSON parsing.

#### API Key Requirements
If you want to run the EarthquakeApp, you'll need your own Google Maps API key:

1. Get a Google Maps API key from the [Google Cloud Console](https://console.cloud.google.com):
   - Create a new project 
   - Enable the Maps SDK for Android
   - Create an API key with appropriate restrictions

2. Create a file at this location:
   ```
   Ch25_EarthquakeApp/EarthquakeWatcher/app/src/main/assets/apikeys.properties
   ```

3. Add your API key to the file in this format:
   ```
   MAPS_API_KEY=YourGoogleMapsAPIKeyHere
   ```

4. The app uses a custom loader to securely load this key at runtime without exposing it in the source code.

### BabyNeedsApp
A shopping list app with database operations and RecyclerView.

### Material Design and Themes
Examples of Material Design implementation in Android apps.

## Technologies

- Java
- Android SDK
- SQLite
- Firebase/Firestore
- Google Maps API
- Android Room
- RecyclerView
- Fragments
- Shared Preferences

## Getting Started

Each project directory contains a standalone Android application. To run any project:

1. Clone this repository
2. Open the specific project folder in Android Studio
3. Sync Gradle files
4. For projects requiring API keys (like EarthquakeApp), follow the specific instructions above
5. Run the application on an emulator or physical device

## Requirements

- Android Studio
- Minimum SDK version varies by project (typically API level 21+)
- Java Development Kit (JDK)
- Google Maps API key (for EarthquakeApp)

## License

This project contains learning materials from The Comprehensive Android Developer Bootcamp by O'Reilly, instructed by Paulo Dichone. Personal implementations of coursework are included for educational purposes.

## Acknowledgements

Special thanks to Paulo Dichone for his excellent instruction and comprehensive curriculum that made learning Android development engaging and practical.
