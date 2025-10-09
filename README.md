# Progr3SS Habit Planner & Tracker

A comprehensive Android habit tracking application built with Kotlin, Jetpack Compose, and Clean Architecture principles.

## 📱 Features

### Core Features
- **User Authentication**: Local registration and login with JWT-based authentication
- **Splash Screen**: Animated splash with auto-login functionality
- **Home Dashboard**: View daily schedules sorted by time with completion status
- **Schedule Management**: 
  - Create schedules with customizable time slots
  - Set repeat patterns (Daily, Weekly, Monthly)
  - Edit and delete schedules
  - View detailed schedule information
- **Habit Management**:
  - Create custom habits with categories and goals
  - Track habit progress
  - View habit history
- **Progress Tracking**: Update progress and add notes for each habit
- **Profile Management**: 
  - View and edit user profile
  - Upload profile images
  - Logout functionality
- **AI Assistant**: Get habit suggestions and health tips (integration ready)

### Technical Features
- **MVVM Architecture** with Repository Pattern
- **Jetpack Compose** for modern, declarative UI
- **Material 3 Design System**
- **Hilt** for Dependency Injection
- **Room Database** for local data persistence
- **Retrofit + OkHttp** for networking
- **StateFlow** for reactive state management
- **Jetpack Navigation** for seamless navigation

## 🏗️ Project Structure

```
app/
├── src/main/java/com/progr3ss/habittracker/
│   ├── data/
│   │   ├── local/
│   │   │   ├── dao/          # Room DAOs
│   │   │   ├── entity/       # Room entities
│   │   │   └── HabitDatabase.kt
│   │   ├── remote/
│   │   │   ├── api/          # Retrofit API services
│   │   │   └── dto/          # Data Transfer Objects
│   │   └── repository/       # Repository implementations
│   ├── di/                   # Hilt dependency injection modules
│   ├── domain/
│   │   └── model/            # Domain models
│   ├── ui/
│   │   ├── navigation/       # Navigation setup
│   │   ├── screens/          # Compose screens
│   │   │   ├── ai/
│   │   │   ├── auth/
│   │   │   ├── habit/
│   │   │   ├── home/
│   │   │   ├── profile/
│   │   │   ├── schedule/
│   │   │   └── splash/
│   │   ├── theme/            # Material 3 theme
│   │   └── viewmodel/        # ViewModels
│   ├── util/                 # Utility classes
│   ├── HabitTrackerApplication.kt
│   └── MainActivity.kt
└── src/main/res/             # Resources (strings, colors, themes)
```

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or newer
- JDK 17 or higher
- Android SDK API 34
- Minimum SDK: API 24 (Android 7.0)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/hun0r1/AndroidProjectHabitTracker.git
   cd AndroidProjectHabitTracker
   ```

2. **Configure API Settings**
   - Copy `.env.example` to `local.properties`
   - Update the API base URL and other credentials:
   ```properties
   API_BASE_URL=https://your-backend-url.com/
   OPENAI_API_KEY=your_openai_key_here
   ```

3. **Open in Android Studio**
   - Open the project in Android Studio
   - Wait for Gradle sync to complete
   - All dependencies will be downloaded automatically

4. **Build and Run**
   - Connect an Android device or start an emulator
   - Click "Run" or press `Shift + F10`

## 🔧 Configuration

### Backend API Endpoints
The app expects the following REST API endpoints:

**Authentication**
- `POST /auth/local/signup` - User registration
- `POST /auth/local/signin` - User login
- `POST /auth/local/refresh` - Refresh access token
- `POST /auth/local/logout` - Logout
- `POST /auth/reset-password-via-email` - Reset password

**Profile**
- `GET /profile` - Get user profile
- `PUT /profile` - Update profile
- `POST /profile/upload-profile-image` - Upload profile image

**Habits**
- `GET /habit` - Get all habits
- `GET /habit/user/{userId}` - Get user habits
- `POST /habit` - Create habit
- `GET /habit/categories` - Get habit categories
- `DELETE /habit/{id}` - Delete habit

**Schedules**
- `GET /schedule/day?date={date}` - Get schedules for a day
- `GET /schedule/{id}` - Get schedule details
- `POST /schedule/day` - Create day schedule
- `POST /schedule/recurring` - Create recurring schedule
- `PUT /schedule/{id}` - Update schedule
- `DELETE /schedule/{id}` - Delete schedule

**Progress**
- `GET /progress?scheduleId={id}` - Get progress for schedule
- `POST /progress` - Create progress entry
- `DELETE /progress/{id}` - Delete progress

### Mock Data
If the backend is not available, the app will use Room database for local caching. You can add mock data or implement a mock API service for testing.

## 📦 Dependencies

### Core
- Kotlin 1.9.20
- Compose BOM 2024.01.00
- Material 3

### Architecture & DI
- Hilt 2.48
- ViewModel & LiveData

### Networking
- Retrofit 2.9.0
- OkHttp 4.12.0
- Gson 2.10.1

### Database
- Room 2.6.1

### Image Loading
- Coil 2.5.0

### Testing
- JUnit 4.13.2
- Mockito 5.8.0
- Coroutines Test 1.7.3

## 🧪 Testing

Run tests with:
```bash
./gradlew test
./gradlew connectedAndroidTest
```

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👥 Contributors

- Developer Team

## 📞 Support

For issues, questions, or contributions, please open an issue on GitHub.

### Build Issues?

If you encounter build errors (especially `checkDebugAarMetadata` failures), see:
- 📘 [TROUBLESHOOTING.md](TROUBLESHOOTING.md) - Comprehensive troubleshooting guide
- 🔧 [BUILD_INSTRUCTIONS.md](BUILD_INSTRUCTIONS.md) - Detailed build instructions

## 🔮 Future Enhancements

- Google Sign-In integration
- Password reset functionality
- Partner/collaborative habits
- Advanced analytics and insights
- Push notifications for habit reminders
- Dark mode support
- Offline-first architecture improvements
