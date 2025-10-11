# Fix for Login/Register ANR Crash - Public/Private Client Architecture

## Problem Statement

The application was experiencing Application Not Responding (ANR) errors and crashes when users pressed the "Login" or "Register" button. 

### Root Cause

The crash was caused by a `runBlocking` call within the `AuthInterceptor` in `NetworkModule.kt`. This interceptor was being applied to **all** network requests, including unauthenticated requests like login and register.

When the user attempted to log in or register:
1. The app would make a network request to the authentication endpoints
2. The `AuthInterceptor` would intercept the request
3. The interceptor would call `runBlocking` to synchronously retrieve the auth token from DataStore
4. This blocked the main UI thread while waiting for the asynchronous DataStore operation
5. The blocking operation triggered an ANR, causing the app to crash

## Solution

We implemented a **public/private client architecture** that separates authenticated and unauthenticated network requests:

### 1. Created Qualifier Annotations

Created `NetworkQualifiers.kt` with two custom Hilt qualifiers:
- `@PublicClient` - For unauthenticated requests
- `@PrivateClient` - For authenticated requests

```kotlin
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PublicClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PrivateClient
```

### 2. Implemented Two OkHttpClient Instances

**Public Client** (No Authentication):
- Does NOT include `AuthInterceptor`
- Only includes `LoggingInterceptor`
- Used for login, register, and other public endpoints

**Private Client** (Authenticated):
- Includes both `AuthInterceptor` and `LoggingInterceptor`
- Used for all protected endpoints that require authentication
- Automatically adds the "Authorization: Bearer <token>" header

### 3. Implemented Two Retrofit Instances

Each Retrofit instance uses its corresponding OkHttpClient:
- `@PublicClient Retrofit` - Uses the public OkHttpClient
- `@PrivateClient Retrofit` - Uses the private OkHttpClient

### 4. Updated API Service Providers

**AuthApiService** (Public):
```kotlin
fun provideAuthApiService(@PublicClient retrofit: Retrofit): AuthApiService
```
- Uses the public Retrofit instance
- No auth interceptor is triggered
- Safe to call from the main thread without ANR

**Other API Services** (Private):
```kotlin
fun provideUserApiService(@PrivateClient retrofit: Retrofit): UserApiService
fun provideHabitApiService(@PrivateClient retrofit: Retrofit): HabitApiService
fun provideScheduleApiService(@PrivateClient retrofit: Retrofit): ScheduleApiService
fun provideProgressApiService(@PrivateClient retrofit: Retrofit): ProgressApiService
```
- Use the private Retrofit instance
- Auth interceptor automatically adds authorization headers
- Only called after successful authentication

## Changes Made

### Files Modified:
1. **NetworkModule.kt** - Updated to provide two separate HTTP client/Retrofit pairs
2. **NetworkQualifiers.kt** (New) - Created qualifier annotations

### Files Created:
1. **NetworkModuleTest.kt** - Unit tests to verify the public/private architecture

## How It Works

### Login/Register Flow (Public Client):
```
User clicks Login/Register
    ↓
AuthApiService receives request (@PublicClient)
    ↓
Public Retrofit Instance
    ↓
Public OkHttpClient (NO auth interceptor)
    ↓
Direct network call (no token retrieval)
    ↓
✅ Success - no ANR
```

### Protected Endpoint Flow (Private Client):
```
App makes authenticated request
    ↓
UserApiService/HabitApiService/etc. receives request (@PrivateClient)
    ↓
Private Retrofit Instance
    ↓
Private OkHttpClient (WITH auth interceptor)
    ↓
AuthInterceptor adds "Authorization: Bearer <token>" header
    ↓
Network call with authentication
    ↓
✅ Success
```

## Benefits

1. **Eliminates ANR on Login/Register**: Public endpoints no longer trigger the auth interceptor
2. **Maintains Security**: Protected endpoints still require and automatically include auth tokens
3. **Separation of Concerns**: Clear distinction between public and authenticated requests
4. **Type Safety**: Hilt qualifiers prevent accidental misuse at compile time
5. **Minimal Code Changes**: Existing repository and ViewModel code requires no modifications

## Testing

Created `NetworkModuleTest.kt` with tests to verify:
- Public client has only logging interceptor (1 interceptor)
- Private client has both auth and logging interceptors (2 interceptors)
- Public and private clients are different instances
- Retrofit instances use the correct clients

## Migration Notes

No changes required in:
- Repository classes
- ViewModel classes
- UI components
- API service interfaces

The dependency injection framework handles everything automatically through the qualifier annotations.

## Technical Details

### Previous Architecture (Problematic):
- Single OkHttpClient with AuthInterceptor
- AuthInterceptor applied to ALL requests
- `runBlocking` called on every request (including login/register)
- Main thread blocked waiting for DataStore

### New Architecture (Fixed):
- Two OkHttpClients (public and private)
- AuthInterceptor only on private client
- Public requests bypass auth logic entirely
- No main thread blocking for unauthenticated requests

## Verification

To verify the fix works:
1. Build and run the application
2. Navigate to login screen
3. Enter credentials and press Login button
4. ✅ App should login successfully without ANR
5. After login, try accessing protected features (habits, schedules)
6. ✅ Authenticated requests should work with automatic token injection

## Related Issues

This fix addresses:
- ANR errors on login
- ANR errors on registration
- Main thread network policy violations
- DataStore blocking main thread

## Future Considerations

1. Consider removing the `runBlocking` from AuthInterceptor entirely by using a different approach for token management
2. Implement token refresh logic in the interceptor
3. Add retry logic for failed authenticated requests
