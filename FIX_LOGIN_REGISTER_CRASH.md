# Fix for Login/Register Button Crash Issue

## Problem Description
The app was crashing immediately when users pressed the Login or Register buttons. The character restrictions (like password must be 8 characters) were working correctly, but the app would crash as soon as the actual login/register action was attempted.

## Root Cause
The issue was caused by **NetworkOnMainThreadException** - a common Android error that occurs when network operations are performed on the Main (UI) thread.

### Technical Details
1. **Repository Flow Methods**: All repository methods that make network calls were using the `flow { }` builder
2. **No Dispatcher Specification**: These Flows didn't specify which thread/dispatcher to run on
3. **Default Behavior**: Flows run on the caller's context by default
4. **ViewModel Context**: The ViewModel was calling these methods from `viewModelScope.launch`, which uses the Main dispatcher
5. **Network on Main Thread**: This caused network calls to execute on the Main thread, triggering a crash

```kotlin
// BEFORE (Problematic)
suspend fun login(email: String, password: String): Flow<Resource<User>> = flow {
    emit(Resource.Loading())
    val response = authApi.login(LoginRequest(email, password)) // ❌ Runs on Main thread!
    // ... rest of the code
}

// AFTER (Fixed)
suspend fun login(email: String, password: String): Flow<Resource<User>> = flow {
    emit(Resource.Loading())
    val response = authApi.login(LoginRequest(email, password)) // ✅ Runs on IO thread
    // ... rest of the code
}.flowOn(Dispatchers.IO) // ✅ Forces execution on IO dispatcher
```

## Changes Made

### 1. AuthRepository.kt
Added `.flowOn(Dispatchers.IO)` to all network-calling methods:
- `login()`
- `register()`
- `logout()`
- `resetPassword()`
- `getProfile()`
- `updateProfile()`

### 2. HabitRepository.kt
Added `.flowOn(Dispatchers.IO)` to:
- `fetchUserHabits()`
- `createHabit()`
- `deleteHabit()`
- `getCategories()`

### 3. ScheduleRepository.kt
Added `.flowOn(Dispatchers.IO)` to:
- `fetchSchedulesByDate()`
- `createSchedule()`
- `updateSchedule()`
- `deleteSchedule()`

### 4. NetworkModule.kt (AuthInterceptor)
Added try-catch protection around DataStore token retrieval to prevent crashes if DataStore is temporarily unavailable:
```kotlin
try {
    val token = runBlocking {
        preferencesManager.accessToken.first()
    }
    if (token != null) {
        request.addHeader("Authorization", "Bearer $token")
    }
} catch (e: Exception) {
    // Ignore token retrieval errors - proceed without auth header
}
```

## Expected Behavior After Fix

### When Backend is NOT Available (Current State)
Since the app is configured to connect to `https://api.example.com/` which doesn't exist:

1. ✅ User can enter email and password
2. ✅ Validation works (password length, required fields, etc.)
3. ✅ **App no longer crashes** when pressing Login/Register
4. ✅ Loading indicator shows briefly
5. ✅ Error message displays: "An error occurred" or specific network error
6. ✅ User remains on the login/register screen

### When Backend IS Available (Production)
1. ✅ User can enter credentials
2. ✅ Validation works
3. ✅ Network call executes on background thread
4. ✅ Success: User navigates to Home screen
5. ✅ Failure: Error message displays

## How to Configure Backend API

To connect to your actual backend, update the `BASE_URL` in:
- File: `app/src/main/java/com/progr3ss/habittracker/di/NetworkModule.kt`
- Line: `private const val BASE_URL = "https://api.example.com/"`

Change it to your backend URL, for example:
```kotlin
private const val BASE_URL = "https://your-backend-api.com/"
```

Alternatively, you can configure it via `local.properties` (though this requires additional implementation to read from properties file).

## Testing the Fix

### Without Backend
1. Build and run the app
2. Navigate to Login or Register screen
3. Enter valid credentials (e.g., email: test@example.com, password: 12345678)
4. Press Login or Register button
5. **Expected**: App shows loading indicator, then displays an error message
6. **Success**: App does NOT crash and user stays on the screen

### With Backend
1. Configure the correct `BASE_URL` in NetworkModule.kt
2. Ensure your backend is running and accessible
3. Try logging in with valid credentials
4. **Expected**: Successful login navigates to Home screen
5. **Expected**: Invalid credentials show appropriate error message

## Why This Happened

Android enforces a strict policy: **network operations must not run on the Main thread**. This prevents the UI from freezing while waiting for network responses. The policy has been enforced since Android 3.0 (API 11).

When network calls are attempted on the Main thread, Android throws `NetworkOnMainThreadException`, which crashes the app if not properly handled.

## Prevention for Future Development

When creating new repository methods that make network calls:

1. Always use `.flowOn(Dispatchers.IO)` for network operations
2. Use `.flowOn(Dispatchers.Default)` for CPU-intensive work
3. Use `.flowOn(Dispatchers.Main)` only for UI operations (though this is usually the default)

### Template for Repository Methods
```kotlin
suspend fun myNetworkCall(): Flow<Resource<Data>> = flow {
    emit(Resource.Loading())
    try {
        val response = api.getData()
        if (response.isSuccessful && response.body() != null) {
            emit(Resource.Success(response.body()!!))
        } else {
            emit(Resource.Error(response.message() ?: "Failed"))
        }
    } catch (e: Exception) {
        emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
    }
}.flowOn(Dispatchers.IO) // ✅ Always add this for network calls
```

## Additional Resources

- [Android Threading Documentation](https://developer.android.com/guide/components/processes-and-threads)
- [Kotlin Coroutines Dispatchers](https://kotlinlang.org/docs/coroutine-context-and-dispatchers.html)
- [Kotlin Flow flowOn Operator](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/flow-on.html)

## Summary

The crash was caused by network calls executing on the Main thread. The fix ensures all network operations run on the IO dispatcher, which is specifically designed for I/O operations like network calls and database access. The app now handles network failures gracefully with error messages instead of crashing.
