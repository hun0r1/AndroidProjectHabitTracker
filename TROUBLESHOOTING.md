# Troubleshooting Guide - Progr3SS Habit Tracker

This guide helps you resolve common build and runtime issues with the Android habit tracking application.

## Table of Contents
- [Build Errors](#build-errors)
- [Gradle Sync Issues](#gradle-sync-issues)
- [Runtime Errors](#runtime-errors)
- [Network Issues](#network-issues)

---

## Build Errors

### Task :app:checkDebugAarMetadata FAILED

**What it is**: This error occurs during the dependency resolution phase when Gradle validates Android Archive (AAR) metadata. The `checkDebugAarMetadata` task ensures all dependencies are compatible with your project configuration.

**Why it happens**:
- **Dependency conflicts**: Multiple libraries depend on different versions of the same dependency
- **SDK version mismatches**: A library requires a higher minSdk or different compileSdk than your project
- **Corrupted downloads**: Incomplete or corrupted dependency files in Gradle cache
- **Network connectivity**: Unable to download required dependencies from remote repositories
- **Gradle cache issues**: Stale or incompatible cached artifacts

**How to fix**:

#### Step 1: Clean Build
```bash
./gradlew clean
./gradlew build --refresh-dependencies
```

#### Step 2: Clear Gradle Cache
```bash
# Clear Gradle caches
rm -rf ~/.gradle/caches/
rm -rf .gradle/

# Clear build directories
rm -rf app/build/
rm -rf build/

# Rebuild
./gradlew build
```

#### Step 3: Get Detailed Error Information
```bash
./gradlew assembleDebug --stacktrace --info | tee build-log.txt
```

Look in the output for:
- Specific library names causing conflicts
- Version numbers that are incompatible
- SDK version requirements

#### Step 4: Check for Specific Conflicts
```bash
# Search for AAR/metadata related errors
./gradlew assembleDebug --info 2>&1 | grep -i "aar\|metadata\|conflict"

# Check dependency tree for conflicts
./gradlew app:dependencies --configuration debugRuntimeClasspath
```

#### Step 5: Verify Network Access
The build requires access to these repositories:
```bash
# Test Google Maven
curl -I https://dl.google.com/

# Test Maven Central
curl -I https://repo.maven.apache.org/maven2/

# Test Gradle Plugin Portal
curl -I https://plugins.gradle.org/
```

If any of these fail, you have a network connectivity issue. See [Network Issues](#network-issues) section.

#### Step 6: Verify SDK Installation
In Android Studio:
1. Open **Tools → SDK Manager**
2. Ensure **Android SDK Platform 34** is installed
3. Ensure **Android SDK Build-Tools 34.0.0+** is installed
4. Click **Apply** if any updates are needed

#### Step 7: Android Studio Cache
If using Android Studio:
1. **File → Invalidate Caches / Restart**
2. Select **Invalidate and Restart**
3. Wait for re-indexing to complete
4. Try building again

#### Step 8: Check Dependency Versions
Review `app/build.gradle.kts` for incompatible dependency versions:

Common issues:
- **Compose BOM**: Ensure using `2024.01.00` or later
- **Kotlin version**: Must match across all dependencies (1.9.0)
- **AGP version**: Android Gradle Plugin 8.1.0 requires Gradle 8.0+

#### Step 9: Examine Specific Library Conflicts
If the error mentions a specific library, check its requirements:
```bash
# Example: Check Room requirements
./gradlew app:dependencyInsight --dependency room-runtime --configuration debugRuntimeClasspath
```

#### Step 10: Last Resort - Reset Everything
```bash
# Delete all Gradle files
rm -rf ~/.gradle/
rm -rf .gradle/
rm -rf app/build/
rm -rf build/

# Re-download Gradle wrapper
./gradlew wrapper --gradle-version 8.2

# Rebuild
./gradlew build
```

---

### Plugin Resolution Failures

**Error**: `Plugin [id: 'com.android.application'] was not found`

**Cause**: The Android Gradle Plugin cannot be downloaded from Google Maven repository.

**Solutions**:

1. **Check network access** to `dl.google.com`:
   ```bash
   curl -I https://dl.google.com/
   ```

2. **Configure proxy** (if behind corporate firewall):
   Edit or create `~/.gradle/gradle.properties`:
   ```properties
   systemProp.http.proxyHost=proxy.company.com
   systemProp.http.proxyPort=8080
   systemProp.https.proxyHost=proxy.company.com
   systemProp.https.proxyPort=8080
   ```

3. **Use a VPN** if the repository is blocked in your region

4. **Offline mode** (requires prior successful build):
   ```bash
   ./gradlew build --offline
   ```

---

### Kotlin Version Conflicts

**Error**: `Module was compiled with an incompatible version of Kotlin`

**Solution**:
Ensure all Kotlin dependencies use version 1.9.0:
- Root `build.gradle.kts`: `kotlin("android") version "1.9.0"`
- KSP plugin: `"1.9.0-1.0.13"`
- Kotlin stdlib, coroutines should auto-align with Kotlin version

---

### Compilation Errors

**Error**: `Cannot find symbol` or `Unresolved reference`

**Causes**:
- Missing KSP-generated code (Hilt, Room)
- Incorrect imports
- Gradle sync incomplete

**Solutions**:

1. **Rebuild project**:
   ```bash
   ./gradlew clean build
   ```

2. **Verify KSP is processing annotations**:
   ```bash
   # Check if Hilt/Room generated code exists
   ls -la app/build/generated/ksp/debug/kotlin/
   ```

3. **In Android Studio**: Build → Rebuild Project

---

## Gradle Sync Issues

### "Build was configured to prefer settings repositories"

**Cause**: Repository declarations in `build.gradle.kts` conflict with centralized repository management.

**Solution**:
Remove any `repositories {}` blocks from `build.gradle.kts` files. All repositories must be configured only in `settings.gradle.kts`.

---

### Slow Gradle Sync

**Solutions**:

1. **Enable Gradle daemon** in `~/.gradle/gradle.properties`:
   ```properties
   org.gradle.daemon=true
   org.gradle.parallel=true
   org.gradle.configureondemand=true
   org.gradle.caching=true
   ```

2. **Increase memory** in `gradle.properties`:
   ```properties
   org.gradle.jvmargs=-Xmx4096m -XX:MaxMetaspaceSize=512m
   ```

---

## Runtime Errors

### App Crashes on Launch

**Check Logcat** in Android Studio or via command line:
```bash
adb logcat | grep -E "AndroidRuntime|progr3ss"
```

Common causes:
- **Missing Room database tables**: Clear app data
- **Network unavailable**: Check API endpoint configuration
- **Hilt injection failure**: Ensure `@HiltAndroidApp` is on Application class

---

### Cannot Connect to Backend API

**Solutions**:

1. **Check API URL** in `local.properties`:
   ```properties
   API_BASE_URL=https://your-backend-api.com/
   ```

2. **Verify backend is running**:
   ```bash
   curl https://your-backend-api.com/api/health
   ```

3. **Use Android Emulator** with localhost:
   - Use `10.0.2.2` instead of `localhost` to access host machine
   - Example: `http://10.0.2.2:8080/api/`

4. **Check network permissions** in `AndroidManifest.xml`:
   ```xml
   <uses-permission android:name="android.permission.INTERNET" />
   ```

---

### Room Database Errors

**Error**: `Cannot find implementation for database`

**Solution**: Rebuild to regenerate Room code:
```bash
./gradlew clean build
```

**Error**: `IllegalStateException: Room cannot verify the data integrity`

**Solution**: Clear app data or uninstall/reinstall:
```bash
adb shell pm clear com.progr3ss.habittracker
# or
adb uninstall com.progr3ss.habittracker
./gradlew installDebug
```

---

## Network Issues

### Cannot Access Google Maven (dl.google.com)

**If in restricted network**:

1. **Use a corporate mirror**: Configure in `settings.gradle.kts`:
   ```kotlin
   repositories {
       maven { url = uri("https://your-company-mirror.com/google") }
       maven { url = uri("https://your-company-mirror.com/maven-central") }
       google()  // fallback
       mavenCentral()  // fallback
   }
   ```

2. **Use Alibaba mirror** (in China):
   ```kotlin
   repositories {
       maven { url = uri("https://maven.aliyun.com/repository/google") }
       maven { url = uri("https://maven.aliyun.com/repository/central") }
       google()
       mavenCentral()
   }
   ```

3. **Pre-download dependencies**: On a machine with access:
   ```bash
   ./gradlew build --refresh-dependencies
   # Copy ~/.gradle/caches/ to restricted machine
   ```

---

### Proxy Configuration

If behind a corporate proxy, configure in `~/.gradle/gradle.properties`:

```properties
systemProp.http.proxyHost=proxy.company.com
systemProp.http.proxyPort=8080
systemProp.http.proxyUser=username
systemProp.http.proxyPassword=password

systemProp.https.proxyHost=proxy.company.com
systemProp.https.proxyPort=8080
systemProp.https.proxyUser=username
systemProp.https.proxyPassword=password

systemProp.http.nonProxyHosts=localhost|127.0.0.1
```

---

## Environment-Specific Issues

### Windows Users

- Use `gradlew.bat` instead of `./gradlew`
- Use backslashes for paths: `app\build\outputs\`
- Git line endings: `git config --global core.autocrlf true`

### macOS Users

- Install Command Line Tools: `xcode-select --install`
- Set JAVA_HOME: `export JAVA_HOME=$(/usr/libexec/java_home -v 17)`

### Linux Users

- Install JDK 17: `sudo apt install openjdk-17-jdk` (Ubuntu/Debian)
- Set JAVA_HOME: `export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64`

---

## Getting More Help

If none of these solutions work:

1. **Collect build information**:
   ```bash
   ./gradlew assembleDebug --stacktrace --info > build-debug.log 2>&1
   ```

2. **Check Gradle version**:
   ```bash
   ./gradlew --version
   ```

3. **Check Java version**:
   ```bash
   java -version
   ```

4. **Open an issue** on GitHub with:
   - Error message (full stacktrace)
   - Build log (build-debug.log)
   - Gradle version
   - Java version
   - Operating system
   - Steps to reproduce

---

## Quick Reference

### Common Commands
```bash
# Clean build
./gradlew clean build

# Refresh dependencies
./gradlew build --refresh-dependencies

# Clear cache and rebuild
rm -rf ~/.gradle/caches/ && ./gradlew build

# Build with detailed output
./gradlew assembleDebug --stacktrace --info

# Check dependencies
./gradlew app:dependencies

# Install debug APK
./gradlew installDebug

# Run tests
./gradlew test
```

### Important Files
- `build.gradle.kts` - Root build configuration
- `app/build.gradle.kts` - App module dependencies
- `settings.gradle.kts` - Repository configuration
- `gradle.properties` - Gradle settings
- `local.properties` - API keys and SDK location (not in git)

---

**Still stuck?** Create an issue at: https://github.com/hun0r1/AndroidProjectHabitTracker/issues
