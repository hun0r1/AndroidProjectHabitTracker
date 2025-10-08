# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Fixed
- Gradle repository configuration error: Removed conflicting repository declarations from build.gradle.kts
- Migrated from legacy buildscript block to modern plugins DSL for better Gradle 8+ compatibility
- Updated to use centralized repository management in settings.gradle.kts with PREFER_SETTINGS mode

### Changed
- Replaced buildscript and allprojects blocks with plugins block in root build.gradle.kts
- Centralized all plugin version declarations at the root level
- Updated clean task to use modern `layout.buildDirectory` API

## [1.0.0] - 2024-10-07

### Added
- Complete Android application structure
- User authentication (Register, Login, Logout)
- JWT-based token management with refresh capability
- Splash screen with auto-login functionality
- Home screen displaying daily schedules
- Schedule management (Create, View, Edit, Delete)
- Habit management (Create, View, Delete)
- Progress tracking data models
- Profile management (View, Edit)
- AI Assistant chat interface
- Material 3 theme with light mode
- Jetpack Compose UI for all screens
- MVVM architecture with Repository Pattern
- Hilt dependency injection
- Room database for local caching
- Retrofit + OkHttp for API communication
- Navigation with Jetpack Navigation Compose
- Form validation with error handling
- StateFlow for reactive state management
- Comprehensive documentation (README, BUILD_INSTRUCTIONS, PROJECT_SUMMARY)
- MIT License
- GitHub Actions CI workflow
- Contributing guidelines

### Technical Details
- Minimum SDK: 24 (Android 7.0)
- Target SDK: 34 (Android 14)
- Kotlin 1.9.0
- Jetpack Compose with Material 3
- Room 2.6.1
- Retrofit 2.9.0
- Hilt 2.48

### API Endpoints Implemented
- Authentication: signup, signin, refresh, logout
- Profile: get, update, upload image
- Habits: CRUD operations, categories
- Schedules: CRUD operations, daily/recurring
- Progress: create, read, delete

### Screens Implemented
1. Splash Screen - animated with auto-login
2. Login Screen - with validation
3. Register Screen - with password matching validation
4. Home Screen - daily schedule list
5. Create Schedule Screen - with time and repeat pattern
6. Schedule Details Screen - with delete confirmation
7. Add Habit Screen - with category and goal
8. Profile Screen - user information with logout
9. Edit Profile Screen - update username
10. AI Assistant Screen - chat interface

### Architecture
- Data Layer: Repository pattern with Room + Retrofit
- Domain Layer: Clean models and business logic
- Presentation Layer: Compose UI + ViewModels
- Dependency Injection: Hilt modules for Database and Network

## [Unreleased]

### Planned Features
- Google Sign-In integration
- Push notifications for habit reminders
- Habit statistics and charts
- Dark theme support
- Export/import data functionality
- Offline-first architecture enhancements
- Unit and integration tests
- UI/instrumentation tests
- Profile image upload functionality
- Password reset email flow
- Habit streaks and achievements
- Social features (partners/collaborative habits)

### Known Issues
- Build requires internet access to Google Maven repository
- No mock data provider for offline testing
- Limited error messages for network failures

### Documentation
- Comprehensive README with setup instructions
- Detailed build instructions
- Project architecture summary
- API endpoint documentation
- Contributing guidelines
