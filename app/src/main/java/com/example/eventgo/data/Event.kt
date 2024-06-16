package com.example.eventgo.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * Data class representing an event.
 * Implements [Parcelable] to allow instances to be passed between Android components.
 * Annotated with [Entity] to indicate that this class represents a SQLite table in Room.
 */
@Parcelize
@Entity(tableName = "events")
data class Event(
    @PrimaryKey val id: String,                  // Primary key for the event
    val name: String,                            // Name of the event
    val artistsTeams: String,                    // Artists or teams involved in the event
    val date: String,                            // Date of the event
    val time: String,                            // Time of the event
    val priceRange: String,                      // Price range for the event tickets
    val ticketStatus: String,                    // Status of the ticket availability
    val buyTicketAt: String,                     // URL to purchase tickets
    val eventSegment: String,                    // Segment of the event (e.g., Music, Sports)
    val eventGenre: String,                      // Genre of the event
    val venueName: String,                       // Name of the venue where the event is held
    val venueAddress: String,                    // Address of the venue
    val venueCity: String,                       // City where the venue is located
    val venueState: String,                      // State where the venue is located
    val venuePhoneNumber: String,                // Phone number of the venue's box office
    val venueOpenHours: String,                  // Open hours of the venue's box office
    val venueGeneralRule: String,                // General rules for the venue
    val venueChildRule: String,                  // Child rules for the venue
    val venueLat: Double,                        // Latitude of the venue location
    val venueLng: Double                         // Longitude of the venue location
) : Parcelable
