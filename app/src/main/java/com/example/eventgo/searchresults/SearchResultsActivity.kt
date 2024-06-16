package com.example.eventgo.searchresults

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventgo.BuildConfig
import com.example.eventgo.EventViewModel
import com.example.eventgo.MainActivity
import com.example.eventgo.data.Event
import com.example.eventgo.databinding.ActivitySearchResultsBinding

/**
 * Activity to display event search results.
 */
class SearchResultsActivity : AppCompatActivity() {

    // Binding object to access views in the layout
    private lateinit var binding: ActivitySearchResultsBinding

    // ViewModel to manage event data
    private val eventViewModel: EventViewModel by viewModels()

    // List to hold selected events
    private val selectedEvents = mutableListOf<Event>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the toolbar
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish() // Close the current activity and return to the previous one
        }

        // Retrieve search parameters from the intent
        val apiKey = BuildConfig.TICKETMASTER_API_KEY
        val keyword = intent.getStringExtra("keyword")
        val segmentId = intent.getStringExtra("category")
        val distance = intent.getStringExtra("distance")
        val distanceUnit = intent.getStringExtra("distanceUnit")
        val location = intent.getStringExtra("geoHash")
        val sort = "date,asc"
        val currentDateTime = intent.getStringExtra("currentDateTime")
        val size = "199"

        // Set up the adapter for the RecyclerView
        val adapter = SearchResultsAdapter(mutableListOf()) { event, isSelected ->
            if (isSelected) {
                selectedEvents.add(event)
            } else {
                selectedEvents.remove(event)
            }
        }

        binding.recyclerViewSearchResults.adapter = adapter
        binding.recyclerViewSearchResults.layoutManager = LinearLayoutManager(this)

        // Fetch events from the API and observe the result
        eventViewModel.fetchEvents(apiKey, keyword, segmentId, distance, distanceUnit, location, sort, currentDateTime, size)
            .observe(this, Observer { events ->
                events?.let {
                    if (it.isNotEmpty()) {
                        adapter.updateResults(it)
                        binding.recyclerViewSearchResults.visibility = View.VISIBLE
                        binding.tvNoResults.visibility = View.GONE
                        binding.fabAddEvent.visibility = View.VISIBLE
                    } else {
                        binding.recyclerViewSearchResults.visibility = View.GONE
                        binding.tvNoResults.visibility = View.VISIBLE
                        binding.fabAddEvent.visibility = View.GONE
                    }
                }
            })

        // Set up the FAB to add selected events to the database and navigate to the MainActivity
        binding.fabAddEvent.setOnClickListener {
            eventViewModel.addEvents(selectedEvents)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
