package com.example.eventgo.eventdetails

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.eventgo.R
import com.example.eventgo.data.Event
import com.example.eventgo.databinding.ActivityEventDetailsBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 * Activity to display event details using a ViewPager and TabLayout.
 */
class EventDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEventDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the toolbar with a back button
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish() // Navigate back to the previous screen
        }

        // Retrieve the event object from the intent
        val event = intent.getParcelableExtra<Event>("event")
        supportActionBar?.title = event?.name
        if (event == null) {
            finish() // If event is null, finish the activity to prevent null pointer exception
            return
        }

        // Set up ViewPager with the EventDetailsPagerAdapter
        binding.viewPager.adapter = EventDetailsPagerAdapter(this, event)

        // Set up TabLayout with ViewPager using TabLayoutMediator
        TabLayoutMediator(binding.eventDetailTabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Event Details"
                    tab.icon = getDrawable(R.drawable.info)
                }
                1 -> {
                    tab.text = "Venue Details"
                    tab.icon = getDrawable(R.drawable.location)
                }
            }
        }.attach()

        // Sync TabLayout and ViewPager
        binding.eventDetailTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // No action needed
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // No action needed
            }
        })

        // Register a callback to synchronize ViewPager and TabLayout
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.eventDetailTabLayout.selectTab(binding.eventDetailTabLayout.getTabAt(position))
            }
        })
    }
}
