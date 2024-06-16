package com.example.eventgo

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventgo.databinding.ActivityMainBinding
import com.example.eventgo.searchform.SearchFormActivity

/**
 * MainActivity class that represents the main screen of the app
 */
class MainActivity : AppCompatActivity() {

    // View binding for the activity layout
    private lateinit var binding: ActivityMainBinding

    // Adapter for displaying the list of events
    private lateinit var eventItemAdapter: EventItemAdapter

    // ViewModel for managing the data for the UI
    private val eventViewModel: EventViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the event item adapter with an empty list and a remove event callback
        eventItemAdapter = EventItemAdapter(mutableListOf()) { event ->
            eventViewModel.removeEvent(event)
        }

        // Set up the RecyclerView with the adapter and a linear layout manager
        binding.recyclerViewEvents.adapter = eventItemAdapter
        binding.recyclerViewEvents.layoutManager = LinearLayoutManager(this)

        // Set up the FloatingActionButton to start the SearchFormActivity when clicked
        binding.fabSearchEvent.setOnClickListener {
            val intent = Intent(this, SearchFormActivity::class.java)
            startActivity(intent)
        }

        // Observe the allEvents LiveData from the ViewModel and update the adapter when the data changes
        eventViewModel.allEvents.observe(this, Observer { events ->
            events?.let {
                eventItemAdapter.updateEvents(it.toMutableList())
            }
        })
    }
}
