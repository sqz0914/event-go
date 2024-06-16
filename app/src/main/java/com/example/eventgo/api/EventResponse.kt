package com.example.eventgo.api.models

import com.example.eventgo.data.Event

/**
 * Data class representing the response from the Ticketmaster API.
 * It contains an embedded object that holds a list of events.
 */
data class EventResponse(
    val _embedded: EmbeddedEvents? // EmbeddedEvents is nullable to handle cases where no events are found
)

/**
 * Data class representing the embedded events within the Ticketmaster API response.
 * It contains a list of TicketmasterEvent objects.
 */
data class EmbeddedEvents(
    val events: List<TicketmasterEvent> // List of events
)

/**
 * Data class representing an event from the Ticketmaster API.
 * It contains various details about the event.
 */
data class TicketmasterEvent(
    val id: String, // Event ID
    val name: String, // Event name
    val dates: Dates?, // Event dates (nullable)
    val priceRanges: List<PriceRange>?, // List of price ranges (nullable)
    val ticketStatus: String?, // Ticket status (nullable)
    val url: String?, // URL to buy tickets (nullable)
    val classifications: List<Classification>?, // List of classifications (nullable)
    val _embedded: EventEmbedded? // Embedded details about the event (nullable)
) {
    // Converts the TicketmasterEvent to an Event data class used within the application.
    // Provides default values for nullable fields.
    fun toEventItem(): Event {
        val venue = _embedded?.venues?.firstOrNull() // Get the first venue if available
        val priceRange = priceRanges?.firstOrNull() // Get the first price range if available
        return Event(
            id = id,
            name = name,
            date = dates?.start?.localDate ?: "N/A", // Default to "N/A" if date is null
            time = dates?.start?.localTime ?: "N/A", // Default to "N/A" if time is null
            priceRange = priceRange?.let { "${it.min} - ${it.max} ${it.currency}" } ?: "N/A", // Format price range
            ticketStatus = ticketStatus ?: "N/A", // Default to "N/A" if ticket status is null
            buyTicketAt = url ?: "N/A", // Default to "N/A" if URL is null
            artistsTeams = _embedded?.attractions?.joinToString(separator = " | ") { it.name } ?: "N/A", // Join artist/team names
            eventSegment = classifications?.firstOrNull()?.segment?.name ?: "N/A", // Get the first segment name
            eventGenre = classifications?.firstOrNull()?.genre?.name ?: "N/A", // Get the first genre name
            venueName = venue?.name ?: "N/A", // Default to "N/A" if venue name is null
            venueAddress = venue?.address?.line1 ?: "N/A", // Default to "N/A" if venue address is null
            venueCity = venue?.city?.name ?: "N/A", // Default to "N/A" if venue city is null
            venueState = venue?.state?.name ?: "N/A", // Default to "N/A" if venue state is null
            venuePhoneNumber = venue?.boxOfficeInfo?.phoneNumberDetail ?: "N/A", // Default to "N/A" if phone number is null
            venueOpenHours = venue?.boxOfficeInfo?.openHoursDetail ?: "N/A", // Default to "N/A" if open hours are null
            venueGeneralRule = venue?.generalInfo?.generalRule ?: "N/A", // Default to "N/A" if general rule is null
            venueChildRule = venue?.generalInfo?.childRule ?: "N/A", // Default to "N/A" if child rule is null
            venueLat = venue?.location?.latitude?.toDouble() ?: 0.0, // Default to 0.0 if latitude is null
            venueLng = venue?.location?.longitude?.toDouble() ?: 0.0 // Default to 0.0 if longitude is null
        )
    }
}

/**
 * Data class representing the dates of an event.
 */
data class Dates(
    val start: Start // Start date and time of the event
)

/**
 * Data class representing the start date and time of an event.
 */
data class Start(
    val localDate: String, // Local date of the event
    val localTime: String // Local time of the event
)

/**
 * Data class representing the classification of an event.
 */
data class Classification(
    val segment: Segment, // Segment classification
    val genre: Genre // Genre classification
)

/**
 * Data class representing the segment classification.
 */
data class Segment(
    val name: String // Name of the segment
)

/**
 * Data class representing the genre classification.
 */
data class Genre(
    val name: String // Name of the genre
)

/**
 * Data class representing the embedded details of an event.
 * It contains lists of venues and attractions.
 */
data class EventEmbedded(
    val venues: List<Venue>?, // List of venues (nullable)
    val attractions: List<Attraction>? // List of attractions (nullable)
)

/**
 * Data class representing a venue of an event.
 */
data class Venue(
    val name: String, // Venue name
    val address: Address, // Venue address
    val city: City, // Venue city
    val state: State, // Venue state
    val boxOfficeInfo: BoxOfficeInfo, // Box office information
    val generalInfo: GeneralInfo, // General information
    val location: Location // Location coordinates
)

/**
 * Data class representing the address of a venue.
 */
data class Address(
    val line1: String // Address line 1
)

/**
 * Data class representing the city of a venue.
 */
data class City(
    val name: String // City name
)

/**
 * Data class representing the state of a venue.
 */
data class State(
    val name: String // State name
)

/**
 * Data class representing the box office information of a venue.
 */
data class BoxOfficeInfo(
    val phoneNumberDetail: String, // Phone number details
    val openHoursDetail: String // Open hours details
)

/**
 * Data class representing the general information of a venue.
 */
data class GeneralInfo(
    val generalRule: String, // General rule information
    val childRule: String // Child rule information
)

/**
 * Data class representing the location coordinates of a venue.
 */
data class Location(
    val longitude: String, // Longitude coordinate
    val latitude: String // Latitude coordinate
)

/**
 * Data class representing an attraction at an event.
 */
data class Attraction(
    val name: String // Name of the attraction
)

/**
 * Data class representing the price range of an event.
 */
data class PriceRange(
    val min: Double, // Minimum price
    val max: Double, // Maximum price
    val currency: String // Currency of the price
)
