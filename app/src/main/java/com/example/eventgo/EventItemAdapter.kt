package com.example.eventgo

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eventgo.data.Event
import com.example.eventgo.eventdetails.EventDetailsActivity

/**
 * Adapter class for managing and displaying a list of event items in a RecyclerView
 */
class EventItemAdapter(
    private val events: MutableList<Event>,
    private val onDeleteClick: (Event) -> Unit
) : RecyclerView.Adapter<EventItemViewHolder>() {

    // Called when RecyclerView needs a new ViewHolder of the given type to represent an item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventItemViewHolder {
        // Inflate the item_event layout and create a ViewHolder
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        view.findViewById<View>(R.id.delete_event).visibility = View.VISIBLE
        view.findViewById<View>(R.id.select_event).visibility = View.GONE
        return EventItemViewHolder(view, onDeleteClick)
    }

    // Called by RecyclerView to display the data at the specified position
    override fun onBindViewHolder(holder: EventItemViewHolder, position: Int) {
        val event = events[position]
        // Bind the event data to the ViewHolder
        holder.bind(event)
        // Set click listener for the itemView to navigate to the event details
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, EventDetailsActivity::class.java).apply {
                putExtra("event", event)
            }
            context.startActivity(intent)
        }
    }

    // Returns the total number of items in the data set held by the adapter
    override fun getItemCount(): Int = events.size

    // Updates the list of events and notifies the adapter to refresh the RecyclerView
    fun updateEvents(newEvents: MutableList<Event>) {
        events.clear()
        events.addAll(newEvents)
        notifyDataSetChanged()
    }
}
