# Build Instructions

## Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- JDK 17 or higher
- Android SDK with API 34 installed
- Internet connection for downloading dependencies

## Build Steps

### 1. Clone and Configure

```bash
git clone https://github.com/hun0r1/AndroidProjectHabitTracker.git
cd AndroidProjectHabitTracker
```

### 2. Configure API Settings

Copy the example environment file and configure your API settings:

```bash
cp .env.example local.properties
```

Edit `local.properties` and set your API configuration:

```properties
API_BASE_URL=https://your-backend-api.com/
OPENAI_API_KEY=your_openai_key_here
```

### 3. Open in Android Studio

1. Launch Android Studio
2. Select "Open an Existing Project"
3. Navigate to the cloned repository directory
4. Click "OK"

### 4. Sync Gradle

Android Studio will automatically start syncing Gradle. If not:
- Click on "File" → "Sync Project with Gradle Files"
- Wait for all dependencies to download

### 5. Build the Project

#### Using Android Studio:
- Click "Build" → "Make Project" (Ctrl+F9 / Cmd+F9)

#### Using Command Line:
```bash
./gradlew build
```

### 6. Run the App

#### Using Android Studio:
1. Connect an Android device via USB or start an Android emulator
2. Select your device from the dropdown
3. Click the "Run" button (Shift+F10 / Ctrl+R)

#### Using Command Line:
```bash
./gradlew installDebug
```

## Troubleshooting

### Gradle Sync Issues

If you encounter Gradle sync issues:

1. **Check Internet Connection**: Ensure you have access to:
   - dl.google.com (for Android Gradle Plugin)
   - repo.maven.apache.org (for Maven dependencies)

2. **Clear Gradle Cache**:
   ```bash
   ./gradlew clean
   rm -rf ~/.gradle/caches/
   ```

3. **Invalidate Caches in Android Studio**:
   - File → Invalidate Caches / Restart → Invalidate and Restart

### SDK Issues

Ensure Android SDK is properly installed:
- Open SDK Manager in Android Studio
- Install Android SDK Platform 34
- Install Android SDK Build-Tools 34.0.0+

### Build Errors

If you see compilation errors:
1. Ensure JDK 17 is being used
2. Check that JAVA_HOME is set correctly
3. Verify all source files are present in the correct directories

### Repository Configuration

This project uses centralized repository management via `settings.gradle.kts` with `RepositoriesMode.PREFER_SETTINGS`. 

**Important**: Do NOT add repository declarations in `build.gradle.kts` files. All repositories are configured in `settings.gradle.kts`:
- `pluginManagement.repositories` - for Gradle plugins
- `dependencyResolutionManagement.repositories` - for project dependencies

If you see errors like "Build was configured to prefer settings repositories over project repositories", remove any `repositories {}` blocks from build.gradle.kts files.

### checkDebugAarMetadata Error

If you encounter the error `Task :app:checkDebugAarMetadata FAILED`, this typically indicates:

**What it is**: The `checkDebugAarMetadata` task validates Android Archive (AAR) metadata to ensure all dependencies are compatible with your project's configuration.

**Common Causes**:
1. **Dependency Version Conflicts**: Different libraries require incompatible versions of the same dependency
2. **Min/Target SDK Mismatch**: A library requires a higher minSdk than your project specifies
3. **Compile SDK Mismatch**: Libraries compiled with a different Android SDK version
4. **Network/Download Issues**: Corrupted or incomplete dependency downloads
5. **Gradle Cache Issues**: Stale or corrupted Gradle cache

**Solutions**:

1. **Clean and Rebuild**:
   ```bash
   ./gradlew clean
   ./gradlew build --refresh-dependencies
   ```

2. **Clear Gradle Cache**:
   ```bash
   rm -rf ~/.gradle/caches/
   rm -rf .gradle/
   ./gradlew build
   ```

3. **Check for Detailed Error Information**:
   ```bash
   ./gradlew assembleDebug --stacktrace --info
   ```
   Look for messages about specific library conflicts or SDK version issues.

4. **Verify Network Access**: Ensure you can access:
   - `dl.google.com` (Google Maven Repository)
   - `repo.maven.apache.org` (Maven Central)
   
   Test with: `curl -I https://dl.google.com/`

5. **Update Dependencies**: If you see version conflicts, update to compatible versions in `app/build.gradle.kts`

6. **Check Min SDK Requirements**: Ensure your `minSdk = 24` is sufficient for all dependencies. Some libraries may require higher versions.

7. **In Android Studio**:
   - File → Invalidate Caches / Restart
   - Tools → SDK Manager → Install Android SDK Platform 34 if not present

**Still Having Issues?**
Run with `--info` flag and look for the specific library causing the conflict:
```bash
./gradlew assembleDebug --info 2>&1 | grep -i "aar\|metadata\|conflict"
```

## Running Tests

```bash
# Unit tests
./gradlew test

# Instrumented tests (requires connected device/emulator)
./gradlew connectedAndroidTest
```

## Building Release APK

```bash
./gradlew assembleRelease
```

The APK will be generated at: `app/build/outputs/apk/release/app-release-unsigned.apk`

## Network Requirements

The project requires access to the following repositories during build:
- Google Maven Repository (dl.google.com)
- Maven Central (repo.maven.apache.org)
- Gradle Plugin Portal (plugins.gradle.org)

If building in a restricted network environment, you may need to:
1. Configure proxy settings in `gradle.properties`
2. Use a local Maven mirror
3. Pre-download dependencies to a local repository

## Next Steps After Build

1. Configure your backend API URL in the app
2. Test authentication flow
3. Customize the theme colors in `ui/theme/Color.kt`
4. Add your app icons to the `res/mipmap-*` directories
5. Configure signing keys for release builds

For more information, see the main [README.md](README.md)
