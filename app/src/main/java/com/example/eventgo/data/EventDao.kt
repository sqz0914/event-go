package com.example.eventgo

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.eventgo.data.Event

/**
 * Data Access Object (DAO) for the Event entity.
 * Defines the database operations that can be performed on the Event table.
 */
@Dao
interface EventDao {

    // Retrieves all events from the database.
    // The return type is LiveData, so the results are observed for changes.
    @Query("SELECT * FROM events")
    fun getAllEvents(): LiveData<List<Event>>

    // Inserts a single event into the database.
    // If there is a conflict (e.g., an event with the same ID already exists), it will be replaced.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event)

    // Inserts a list of events into the database.
    // If there is a conflict (e.g., events with the same IDs already exist), they will be replaced.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<Event>)

    // Deletes a single event from the database by its ID.
    @Query("DELETE FROM events WHERE id = :eventId")
    suspend fun deleteEvent(eventId: String)
}
