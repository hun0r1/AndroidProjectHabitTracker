# Network Architecture Diagram

## Before (Problematic)

```
┌─────────────────────────────────────────────────────────────┐
│                     All API Services                        │
│  AuthApiService, UserApiService, HabitApiService, etc.      │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
                 ┌───────────────┐
                 │    Retrofit   │
                 └───────┬───────┘
                         │
                         ▼
                 ┌───────────────┐
                 │  OkHttpClient │
                 └───────┬───────┘
                         │
        ┌────────────────┼────────────────┐
        │                │                │
        ▼                ▼                ▼
┌───────────────┐ ┌──────────────┐ ┌──────────┐
│ AuthInterceptor│ │ LogInterceptor│ │ Network  │
│ (runBlocking!) │ │              │ │          │
└───────┬────────┘ └──────────────┘ └──────────┘
        │
        ▼
  ❌ BLOCKS MAIN THREAD
     on ALL requests
    (including login!)
```

## After (Fixed)

```
┌──────────────────┐                    ┌────────────────────────────────┐
│  AuthApiService  │                    │  UserApiService, HabitApiService│
│ (login/register) │                    │  ScheduleApiService, etc.      │
└────────┬─────────┘                    └────────────┬───────────────────┘
         │                                           │
         │                                           │
         ▼                                           ▼
   ┌──────────────┐                          ┌──────────────┐
   │@PublicClient │                          │@PrivateClient│
   │   Retrofit   │                          │   Retrofit   │
   └──────┬───────┘                          └──────┬───────┘
          │                                         │
          ▼                                         ▼
   ┌──────────────┐                          ┌──────────────┐
   │@PublicClient │                          │@PrivateClient│
   │ OkHttpClient │                          │ OkHttpClient │
   └──────┬───────┘                          └──────┬───────┘
          │                                         │
          │                                         │
          ▼                                ┌────────┴────────┐
   ┌──────────────┐                       │                 │
   │LogInterceptor│                       ▼                 ▼
   └──────┬───────┘              ┌──────────────┐   ┌──────────────┐
          │                      │AuthInterceptor│   │LogInterceptor│
          ▼                      │ (runBlocking)│   └──────────────┘
      ┌────────┐                 └──────┬───────┘
      │Network │                        │
      └────────┘                        ▼
                                  ┌────────────┐
  ✅ No blocking                  │  Network   │
     on login/                    └────────────┘
     register!
                                  ✅ Auth token added
                                     for protected
                                     endpoints
```

## Key Differences

### Public Client (Unauthenticated)
- ✅ No AuthInterceptor
- ✅ No `runBlocking` call
- ✅ No main thread blocking
- ✅ Safe for login/register
- Uses: Login, Register, Password Reset

### Private Client (Authenticated)
- ✅ Has AuthInterceptor
- ✅ Automatically adds auth token
- ✅ Only called after authentication
- ✅ Protects user data
- Uses: User profile, Habits, Schedules, Progress

## Dependency Injection Flow

```
NetworkModule provides:

1. LoggingInterceptor (shared)
2. AuthInterceptor (private only)

3. PublicOkHttpClient
   └─> Uses: LoggingInterceptor

4. PrivateOkHttpClient
   └─> Uses: LoggingInterceptor + AuthInterceptor

5. PublicRetrofit(@PublicClient)
   └─> Uses: PublicOkHttpClient

6. PrivateRetrofit(@PrivateClient)
   └─> Uses: PrivateOkHttpClient

7. AuthApiService
   └─> Injects: @PublicClient Retrofit

8. Other API Services
   └─> Inject: @PrivateClient Retrofit
```

## Request Flow Examples

### Login Request
```
LoginScreen
  → LoginViewModel
    → AuthRepository
      → AuthApiService (@PublicClient)
        → PublicRetrofit
          → PublicOkHttpClient
            → LoggingInterceptor (only)
              → Network Call
                ✅ Success (no ANR)
```

### Get User Habits (Authenticated)
```
HabitsScreen
  → HabitsViewModel
    → HabitRepository
      → HabitApiService (@PrivateClient)
        → PrivateRetrofit
          → PrivateOkHttpClient
            → AuthInterceptor (adds token)
            → LoggingInterceptor
              → Network Call with "Authorization: Bearer <token>"
                ✅ Success (authenticated)
```
