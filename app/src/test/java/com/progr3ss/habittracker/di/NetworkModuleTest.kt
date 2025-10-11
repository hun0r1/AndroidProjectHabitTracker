package com.progr3ss.habittracker.di

import com.progr3ss.habittracker.util.PreferencesManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import retrofit2.Retrofit

/**
 * Test for NetworkModule to verify the public/private client architecture
 */
class NetworkModuleTest {

    private lateinit var loggingInterceptor: HttpLoggingInterceptor
    private lateinit var preferencesManager: PreferencesManager

    @Before
    fun setUp() {
        loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.NONE
        }
        preferencesManager = mock(PreferencesManager::class.java)
    }

    @Test
    fun `provideLoggingInterceptor should return HttpLoggingInterceptor`() {
        val interceptor = NetworkModule.provideLoggingInterceptor()
        assertNotNull(interceptor)
        assertTrue(interceptor is HttpLoggingInterceptor)
    }

    @Test
    fun `provideAuthInterceptor should return Interceptor`() {
        val interceptor = NetworkModule.provideAuthInterceptor(preferencesManager)
        assertNotNull(interceptor)
    }

    @Test
    fun `providePublicOkHttpClient should not include auth interceptor`() {
        val client = NetworkModule.providePublicOkHttpClient(loggingInterceptor)
        assertNotNull(client)
        assertTrue(client is OkHttpClient)
        
        // Public client should have only 1 interceptor (logging)
        // Note: OkHttp adds internal interceptors, so we check application interceptors
        assertEquals(1, client.interceptors.size)
    }

    @Test
    fun `providePrivateOkHttpClient should include auth interceptor`() {
        val authInterceptor = NetworkModule.provideAuthInterceptor(preferencesManager)
        val client = NetworkModule.providePrivateOkHttpClient(loggingInterceptor, authInterceptor)
        assertNotNull(client)
        assertTrue(client is OkHttpClient)
        
        // Private client should have 2 interceptors (auth + logging)
        assertEquals(2, client.interceptors.size)
    }

    @Test
    fun `providePublicRetrofit should use public client`() {
        val publicClient = NetworkModule.providePublicOkHttpClient(loggingInterceptor)
        val retrofit = NetworkModule.providePublicRetrofit(publicClient)
        assertNotNull(retrofit)
        assertTrue(retrofit is Retrofit)
        assertEquals(publicClient, retrofit.callFactory())
    }

    @Test
    fun `providePrivateRetrofit should use private client`() {
        val authInterceptor = NetworkModule.provideAuthInterceptor(preferencesManager)
        val privateClient = NetworkModule.providePrivateOkHttpClient(loggingInterceptor, authInterceptor)
        val retrofit = NetworkModule.providePrivateRetrofit(privateClient)
        assertNotNull(retrofit)
        assertTrue(retrofit is Retrofit)
        assertEquals(privateClient, retrofit.callFactory())
    }

    @Test
    fun `public and private clients should be different instances`() {
        val authInterceptor = NetworkModule.provideAuthInterceptor(preferencesManager)
        val publicClient = NetworkModule.providePublicOkHttpClient(loggingInterceptor)
        val privateClient = NetworkModule.providePrivateOkHttpClient(loggingInterceptor, authInterceptor)
        
        assertNotEquals(publicClient, privateClient)
        // Verify different number of interceptors
        assertNotEquals(publicClient.interceptors.size, privateClient.interceptors.size)
    }
}
