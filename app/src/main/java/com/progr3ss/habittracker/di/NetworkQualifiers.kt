package com.progr3ss.habittracker.di

import javax.inject.Qualifier

/**
 * Qualifier for public OkHttpClient and Retrofit instances
 * Used for unauthenticated API calls (e.g., login, register)
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PublicClient

/**
 * Qualifier for private OkHttpClient and Retrofit instances
 * Used for authenticated API calls (requires auth token)
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PrivateClient
