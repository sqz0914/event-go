package com.example.eventgo.searchresults

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eventgo.EventItemViewHolder
import com.example.eventgo.R
import com.example.eventgo.data.Event

/**
 * Adapter class for displaying search results in a RecyclerView.
 */
class SearchResultsAdapter(
    private val results: MutableList<Event>,
    private val onEventSelected: (Event, Boolean) -> Unit
) : RecyclerView.Adapter<EventItemViewHolder>() {

    // Inflates the item layout and creates the ViewHolder.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventItemViewHolder {
        // Inflate the item layout for each event
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        // Hide the delete button and show the checkbox for selection
        view.findViewById<View>(R.id.delete_event).visibility = View.GONE
        view.findViewById<View>(R.id.select_event).visibility = View.VISIBLE
        // Return a new ViewHolder instance
        return EventItemViewHolder(view, onSelectChange = onEventSelected)
    }

    // Binds the data to the ViewHolder.
    override fun onBindViewHolder(holder: EventItemViewHolder, position: Int) {
        // Get the event at the current position
        val event = results[position]
        // Bind the event data to the ViewHolder
        holder.bind(event)
    }

    // Returns the total number of items in the data set held by the adapter.
    override fun getItemCount(): Int = results.size

    // Updates the data set and notifies the adapter of data changes.
    fun updateResults(newResults: List<Event>) {
        // Clear the current list and add the new results
        results.clear()
        results.addAll(newResults)
        // Notify the adapter that the data set has changed
        notifyDataSetChanged()
    }
}
