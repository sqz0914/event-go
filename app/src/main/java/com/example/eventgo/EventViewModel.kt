package com.example.eventgo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.eventgo.api.ApiClient
import com.example.eventgo.data.AppDatabase
import com.example.eventgo.data.Event
import kotlinx.coroutines.launch

/**
 * ViewModel class for managing event data
 */
class EventViewModel(application: Application) : AndroidViewModel(application) {

    // DAO for accessing event data
    private val eventDao: EventDao = AppDatabase.getDatabase(application).eventDao()

    // LiveData to observe all events from the database
    val allEvents: LiveData<List<Event>> = eventDao.getAllEvents()

    // Function to add a single event to the database
    fun addEvent(event: Event) {
        viewModelScope.launch {
            eventDao.insertEvent(event)
        }
    }

    // Function to add multiple events to the database
    fun addEvents(events: List<Event>) {
        viewModelScope.launch {
            eventDao.insertEvents(events)
        }
    }

    // Function to remove an event from the database
    fun removeEvent(event: Event) {
        viewModelScope.launch {
            eventDao.deleteEvent(event.id)
        }
    }

    // Function to fetch events from Ticketmaster API and return LiveData
    fun fetchEvents(
        apiKey: String,
        keyword: String?,
        segmentId: String?,
        radius: String?,
        unit: String?,
        geoPoint: String?,
        sort: String?,
        startDateTime: String?,
        size: String?
    ): LiveData<List<Event>> {
        val liveData = MutableLiveData<List<Event>>()
        viewModelScope.launch {
            try {
                // Make API call to fetch events
                val response = ApiClient.apiService.searchEvents(apiKey, keyword, segmentId, radius, unit, geoPoint, sort, startDateTime, size)
                // Map the response to Event objects and post the value to LiveData
                val events = response._embedded?.events?.map { it.toEventItem() } ?: emptyList()
                liveData.postValue(events)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return liveData
    }
}
