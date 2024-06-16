package com.example.eventgo.api

import com.example.eventgo.api.models.EventResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface for the Ticketmaster API service.
 * Defines the endpoints and query parameters for the API.
 */
interface TicketmasterApiService {

    // Searches for events using the specified query parameters.
    @GET("events")
    suspend fun searchEvents(
        @Query("apikey") apiKey: String, // API key for authentication
        @Query("keyword") keyword: String?, // Keyword to search for events (nullable)
        @Query("segmentId") category: String?, // Segment ID to filter events by category (nullable)
        @Query("radius") radius: String?, // Search radius (nullable)
        @Query("unit") unit: String?, // Unit of measurement for the radius (nullable)
        @Query("geoPoint") geoPoint: String?, // Geohash representing the location (nullable)
        @Query("sort") sort: String?, // Sort order for the events (nullable)
        @Query("startDateTime") startDateTime: String?, // Start date and time to filter events (nullable)
        @Query("size") size: String? // Number of events to retrieve (nullable)
    ): EventResponse
}
