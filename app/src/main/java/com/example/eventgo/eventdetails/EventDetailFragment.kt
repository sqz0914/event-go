package com.example.eventgo.eventdetails

import android.graphics.Typeface
import android.os.Bundle
import android.text.method.LinkMovementMethod
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
import com.example.eventgo.databinding.FragmentEventDetailBinding

/**
 * Fragment to display the details of an event.
 */
class EventDetailFragment : Fragment() {

    // Binding object instance for fragment_event_detail.xml layout
    private var _binding: FragmentEventDetailBinding? = null
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
        _binding = FragmentEventDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        // Add event details to the table layout if they are not "N/A"
        if (event.artistsTeams != "N/A") {
            addTableRow(binding.eventDetailTable, "Artists/Teams", event.artistsTeams, false)
        }
        if(event.eventSegment != "N/A") {
            addTableRow(binding.eventDetailTable, "Category", "${event.eventSegment} | ${event.eventGenre}", false)
        }
        if(event.venueName != "N/A") {
            addTableRow(binding.eventDetailTable, "Venue", event.venueName, false)
        }
        if(event.date != "N/A") {
            addTableRow(binding.eventDetailTable, "Date", event.date, false)
        }
        if(event.time != "N/A") {
            addTableRow(binding.eventDetailTable, "Time", event.time, false)
        }
        if(event.priceRange != "N/A") {
            addTableRow(binding.eventDetailTable, "Price Range", event.priceRange, false)
        }
        if(event.ticketStatus != "N/A") {
            addTableRow(binding.eventDetailTable, "Ticket Status", event.ticketStatus, false)
        }
        if(event.buyTicketAt != "N/A") {
            addTableRow(binding.eventDetailTable, "Buy Ticket At", "Click Here", true, event.buyTicketAt)
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

    // Creates a new instance of EventDetailFragment with the given event data.
    companion object {
        @JvmStatic
        fun newInstance(event: Event) =
            EventDetailFragment().apply {
                // Pass the event data to the fragment using arguments
                arguments = Bundle().apply {
                    putParcelable("event", event)
                }
            }
    }
}
