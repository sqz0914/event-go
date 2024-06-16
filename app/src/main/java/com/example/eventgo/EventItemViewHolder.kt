package com.example.eventgo

import android.content.Intent
import android.view.View
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eventgo.data.Event
import com.example.eventgo.eventdetails.EventDetailsActivity

/**
 * ViewHolder class for displaying event items in a RecyclerView
 */
class EventItemViewHolder(
    itemView: View,
    private val onDeleteClick: ((Event) -> Unit)? = null,
    private val onSelectChange: ((Event, Boolean) -> Unit)? = null
) : RecyclerView.ViewHolder(itemView) {

    // View references
    private val eventIcon: ImageView = itemView.findViewById(R.id.event_icon)
    private val eventName: TextView = itemView.findViewById(R.id.event_name)
    private val eventVenue: TextView = itemView.findViewById(R.id.event_venue)
    private val eventDate: TextView = itemView.findViewById(R.id.event_date)
    private val eventTime: TextView = itemView.findViewById(R.id.event_time)
    private val deleteEvent: ImageButton? = itemView.findViewById(R.id.delete_event)
    private val selectEvent: CheckBox? = itemView.findViewById(R.id.select_event)

    // Binds event data to the views
    fun bind(event: Event) {
        eventName.text = event.name
        if(event.venueName == "N/A") {
            eventVenue.visibility = View.GONE
        } else {
            eventVenue.text = event.venueName
        }
        if(event.date == "N/A") {
            eventDate.visibility = View.GONE
        } else {
            eventDate.text = event.date
        }
        if(event.time == "N/A") {
            eventTime.visibility = View.GONE
        } else {
            eventTime.text = event.time
        }
        eventIcon.setImageResource(getIconResource(event.eventSegment))

        // Set click listener for delete button
        deleteEvent?.let {
            it.setOnClickListener { onDeleteClick?.invoke(event) }
        }

        // Set change listener for select checkbox
        selectEvent?.let {
            it.setOnCheckedChangeListener(null) // Prevents the listener from being called during bind
            it.isChecked = false // Reset the checkbox
            it.setOnCheckedChangeListener { _, isChecked -> onSelectChange?.invoke(event, isChecked) }
        }

        // Set click listener for the entire itemView to navigate to event details
        itemView.setOnClickListener {
            val context = itemView.context
            val intent = Intent(context, EventDetailsActivity::class.java).apply {
                putExtra("event", event)
            }
            context.startActivity(intent)
        }
    }

    // Returns the icon resource based on the event type
    private fun getIconResource(eventType: String): Int {
        return when (eventType) {
            "Arts & Theatre" -> R.drawable.art
            "Film" -> R.drawable.film
            "Miscellaneous" -> R.drawable.miscellaneous
            "Music" -> R.drawable.music
            "Sports" -> R.drawable.sports
            else -> R.drawable.other // Default icon in case the type doesn't match
        }
    }
}
