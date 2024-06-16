package com.example.eventgo.eventdetails

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.eventgo.data.Event

/**
 * Adapter for managing fragments in the ViewPager of EventDetailsActivity.
 */
class EventDetailsPagerAdapter(
    activity: AppCompatActivity,
    private val event: Event
) : FragmentStateAdapter(activity) {

    // Returns the number of fragments.
    override fun getItemCount(): Int = 2

    // Creates a fragment for the given position.
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> EventDetailFragment.newInstance(event)
            1 -> VenueDetailFragment.newInstance(event)
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}
