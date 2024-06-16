package com.example.eventgo.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton object that provides a Retrofit instance and an API service for accessing the Ticketmaster API.
 */
object ApiClient {

    // Base URL for the Ticketmaster API
    private const val BASE_URL = "https://app.ticketmaster.com/discovery/v2/"

    // Lazy initialization of the Retrofit instance
    private val retrofit: Retrofit by lazy {
        // Create a logging interceptor for monitoring network requests
        val logging = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY) // Log the body of the requests and responses
        }

        // Create an OkHttpClient and add the logging interceptor
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        // Build the Retrofit instance
        Retrofit.Builder()
            .baseUrl(BASE_URL) // Set the base URL for the API
            .client(client) // Set the OkHttpClient with the logging interceptor
            .addConverterFactory(GsonConverterFactory.create()) // Add a converter factory for parsing JSON responses
            .build()
    }

    // Lazy initialization of the API service
    val apiService: TicketmasterApiService by lazy {
        retrofit.create(TicketmasterApiService::class.java) // Create an implementation of the API service
    }
}
