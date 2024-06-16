package com.example.eventgo.eventdetails

import android.graphics.Typeface
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.example.eventgo.R
import com.example.eventgo.data.Event
import com.example.eventgo.databinding.FragmentVenueDetailBinding
import com.example.eventsearchapp.MapFragment

/**
 * Fragment to display the details of a venue.
 */
class VenueDetailFragment : Fragment() {

    // Binding object instance for fragment_venue_detail.xml layout
    private var _binding: FragmentVenueDetailBinding? = null
    // Backing property to avoid direct access to _binding outside of onCreateView and onDestroyView
    private val binding get() = _binding!!

    // Event object to hold the event details
    private lateinit var event: Event

    // Initializes the fragment and retrieves the event data from the arguments.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Retrieve the event object from the fragment arguments
        arguments?.let {
            event = it.getParcelable("event")!!
        }
    }

    // Inflates the layout for this fragment and sets up the UI elements.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using view binding
        _binding = FragmentVenueDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        // Check if venue details are available
        if(event.venueName == "N/A") {
            binding.venueDetailTable.visibility = View.GONE
            binding.tvNoRecord.visibility = View.VISIBLE
        } else {
            // Populate the table with venue details
            addTableRow(binding.venueDetailTable, "Name", event.venueName, false)
            addTableRow(binding.venueDetailTable, "Address", event.venueAddress, false)
            addTableRow(binding.venueDetailTable, "City", event.venueCity, false)
            addTableRow(binding.venueDetailTable, "State", event.venueState, false)

            if(event.venuePhoneNumber != "N/A") {
                addTableRow(binding.venueDetailTable, "Phone Number", event.venuePhoneNumber, false)
            }
            if(event.venueOpenHours != "N/A") {
                addTableRow(binding.venueDetailTable, "Open Hours", event.venueOpenHours, false)
            }
            if(event.venueGeneralRule != "N/A") {
                addTableRow(binding.venueDetailTable, "General Rule", event.venueGeneralRule, false)
            }
            if(event.venueChildRule != "N/A") {
                addTableRow(binding.venueDetailTable, "Child Rule", event.venueChildRule, false)
            }

            // Add MapFragment dynamically to display the venue location on the map
            val lat = event.venueLat
            val lng = event.venueLng
            Log.d("VenueDetailFragment", "$lat  $lng")
            val fragment = MapFragment(lat, lng, event.venueName)
            childFragmentManager.beginTransaction().replace(R.id.google_map, fragment).commit()
        }

        return view
    }

    // Adds a table row with the given label and text content.
    private fun addTableRow(tableLayout: TableLayout, labelName: String, textContent: String, link: Boolean, url: String = "") {
        val context = requireContext()

        // Create a new table row
        val tr = TableRow(context)
        val rowParams = TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT)
        rowParams.setMargins(0, 0, 0, 10) // Set bottom margin for the row
        tr.layoutParams = rowParams
        tr.weightSum = 1f // Set weight sum for the row

        // Create layout parameters for the label cell
        var cellParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT)
        cellParams.setMargins(0, 0, 0, 20) // Set bottom margin for the cell
        cellParams.weight = 0.4f // Set weight for the label cell

        // Create and configure the label TextView
        val label = TextView(context)
        label.layoutParams = cellParams
        label.text = labelName
        label.setTypeface(null, Typeface.BOLD)

        // Create layout parameters for the text content cell
        cellParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT)
        cellParams.setMargins(0, 0, 0, 20) // Set bottom margin for the cell
        cellParams.weight = 0.6f // Set weight for the text content cell

        // Create and configure the text content TextView
        val text = TextView(context)
        text.layoutParams = cellParams

        if (link) {
            // If the text content is a hyperlink, make it clickable and set the link
            text.isClickable = true
            text.movementMethod = LinkMovementMethod.getInstance()
            text.text = HtmlCompat.fromHtml("<a href=\"$url\">$textContent</a>", HtmlCompat.FROM_HTML_MODE_LEGACY)
            text.setLinkTextColor(ContextCompat.getColor(context, R.color.darkBlue))
        } else {
            // If the text content is not a hyperlink, just set the text
            text.text = textContent
        }

        // Add the label and text content to the table row
        tr.addView(label)
        tr.addView(text)
        // Add the table row to the table layout
        tableLayout.addView(tr)
    }

    // Cleans up the binding object when the view is destroyed.
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Set the binding to null to avoid memory leaks
    }

    // Creates a new instance of VenueDetailFragment with the given event data.
    companion object {
        @JvmStatic
        fun newInstance(event: Event) =
            VenueDetailFragment().apply {
                // Pass the event data to the fragment using arguments
                arguments = Bundle().apply {
                    putParcelable("event", event)
                }
            }
    }
}
