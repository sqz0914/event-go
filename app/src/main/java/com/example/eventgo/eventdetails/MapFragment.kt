package com.example.eventsearchapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eventgo.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

/**
 * A simple [Fragment] subclass to display a map with a marker.
 * Use the [MapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MapFragment(private val lat: Double, private val lng: Double, private val venueName: String) : Fragment() {

    // Inflates the layout for this fragment and initializes the map.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        // Get the SupportMapFragment and request notification when the map is ready to be used
        val mapFragment = childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            // Create a LatLng object for the venue
            val venue = LatLng(lat, lng)
            // Add a marker at the venue location with the venue name
            googleMap.addMarker(MarkerOptions().position(venue).title(venueName))
            // Move the camera to the venue location and set zoom level
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(venue, 15f))
        }

        // Return the inflated view
        return view
    }
}
