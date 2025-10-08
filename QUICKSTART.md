# Quick Start Guide - Progr3SS Habit Tracker

Get up and running with Progr3SS Habit Tracker in just a few minutes!

## Prerequisites

Before you begin, make sure you have:
- ✅ Android Studio Hedgehog (2023.1.1) or later
- ✅ JDK 17 or higher
- ✅ Android SDK with API 34
- ✅ Internet connection

## Quick Setup (5 minutes)

### Step 1: Clone the Repository
```bash
git clone https://github.com/hun0r1/AndroidProjectHabitTracker.git
cd AndroidProjectHabitTracker
```

### Step 2: Configure API Settings
```bash
# Copy the example configuration
cp .env.example local.properties

# Edit local.properties with your favorite editor
# Set your API_BASE_URL to your backend API
```

### Step 3: Open in Android Studio
1. Launch Android Studio
2. Click "Open an Existing Project"
3. Select the `AndroidProjectHabitTracker` folder
4. Wait for Gradle sync (first time may take a few minutes)

### Step 4: Run the App
1. Connect an Android device or start an emulator
2. Click the green "Run" button (▶️) or press `Shift + F10`
3. The app will build and install on your device

## What You'll See

### First Launch
1. **Splash Screen** - Beautiful animated splash screen
2. **Login Screen** - Since you're not logged in yet, you'll see the login screen

### Try It Out
1. Click "Don't have an account? Register"
2. Fill in the registration form
3. After successful registration, you'll be taken to the Home screen

### Main Features to Explore
- 📅 **Home** - View your daily schedule
- ➕ **Add Schedule** - Create new habit schedules
- 💪 **Add Habit** - Define new habits
- 👤 **Profile** - View and edit your profile
- 🤖 **AI Assistant** - Get habit suggestions (mock interface)

## Common Issues

### Gradle Sync Failed?
**Solution**: Make sure you have internet access and try:
```bash
./gradlew clean
./gradlew build --refresh-dependencies
```

### Cannot Connect to Backend?
**Solution**: The app expects a live backend. For development:
1. Update `API_BASE_URL` in `local.properties`
2. Or implement mock data in repositories for offline testing

### App Crashes on Launch?
**Solution**: Check Android Studio Logcat for errors. Common issues:
- Missing Room database tables (clear app data)
- Network connectivity issues
- API endpoint not available

## Development Workflow

### Making Changes

1. **Create a Feature Branch**
   ```bash
   git checkout -b feature/my-new-feature
   ```

2. **Make Your Changes**
   - Edit Kotlin files in `app/src/main/java/com/progr3ss/habittracker/`
   - Update Compose UI in `ui/screens/`
   - Add ViewModels in `ui/viewmodel/`

3. **Test Your Changes**
   ```bash
   ./gradlew test
   ```

4. **Run the App**
   - Press `Shift + F10` to run
   - Check that your changes work as expected

### Project Structure at a Glance

```
app/src/main/java/com/progr3ss/habittracker/
├── data/          # Data layer (API, Database, Repositories)
├── domain/        # Domain models
├── ui/            # UI layer (Screens, ViewModels, Theme)
├── di/            # Dependency Injection
└── util/          # Utility classes
```

## Next Steps

### For Users
1. Configure your backend API URL
2. Register an account
3. Start creating habits and schedules
4. Track your progress!

### For Developers
1. Read [CONTRIBUTING.md](CONTRIBUTING.md) for contribution guidelines
2. Check [PROJECT_SUMMARY.md](PROJECT_SUMMARY.md) for detailed architecture
3. See [BUILD_INSTRUCTIONS.md](BUILD_INSTRUCTIONS.md) for advanced build options
4. Explore the codebase and make improvements!

### Customize the App
- **Theme**: Edit colors in `ui/theme/Color.kt`
- **Strings**: Update text in `res/values/strings.xml`
- **App Name**: Change in `res/values/strings.xml`
- **App Icon**: Replace images in `res/mipmap-*` folders

## Useful Commands

```bash
# Build the app
./gradlew build

# Run tests
./gradlew test

# Install debug APK
./gradlew installDebug

# Clean build
./gradlew clean

# Generate release APK
./gradlew assembleRelease
```

## Resources

- 📚 [README.md](README.md) - Full documentation
- 🔧 [BUILD_INSTRUCTIONS.md](BUILD_INSTRUCTIONS.md) - Detailed build guide
- 📖 [PROJECT_SUMMARY.md](PROJECT_SUMMARY.md) - Architecture details
- 🤝 [CONTRIBUTING.md](CONTRIBUTING.md) - How to contribute
- 📝 [CHANGELOG.md](CHANGELOG.md) - Version history

## Need Help?

- 🐛 Found a bug? [Open an issue](https://github.com/hun0r1/AndroidProjectHabitTracker/issues)
- 💡 Have a suggestion? [Start a discussion](https://github.com/hun0r1/AndroidProjectHabitTracker/discussions)
- 📧 Need support? Check existing issues first!

## Success! 🎉

You're now ready to develop with Progr3SS Habit Tracker. Happy coding!

---

**Note**: This is a development build. For production deployment, see [BUILD_INSTRUCTIONS.md](BUILD_INSTRUCTIONS.md) for release configuration.
