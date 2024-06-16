package com.example.eventgo.searchform

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.eventgo.R
import com.example.eventgo.databinding.ActivitySearchFormBinding
import com.example.eventgo.searchresults.SearchResultsActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import ch.hsr.geohash.GeoHash
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Activity to display event search form and ask user inputs.
 */
class SearchFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchFormBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // Category to segment id mapping
    private val categoryToSegmentId = mapOf(
        "All" to null,
        "Music" to "KZFzniwnSyZfZ7v7nJ",
        "Sports" to "KZFzniwnSyZfZ7v7nE",
        "Arts & Theatre" to "KZFzniwnSyZfZ7v7na",
        "Film" to "KZFzniwnSyZfZ7v7nn",
        "Miscellaneous" to "KZFzniwnSyZfZ7v7n1"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the Toolbar and set up the back button
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish() // This will close the current activity and return to the previous one
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Set up the category spinner
        val categories = arrayOf("All", "Music", "Sports", "Arts & Theatre", "Film", "Miscellaneous")
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = categoryAdapter

        // Set up the distance unit spinner
        val distanceUnits = arrayOf("miles", "km")
        val distanceUnitAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, distanceUnits)
        distanceUnitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDistanceUnit.adapter = distanceUnitAdapter

        // Set default selection for RadioGroup
        binding.rgLocation.check(R.id.rb_current_location)

        // Handle RadioGroup changes
        binding.rgLocation.setOnCheckedChangeListener { _, checkedId ->
            binding.etSpecifyLocation.visibility = if (checkedId == R.id.rb_specify_location) View.VISIBLE else View.GONE
        }

        // Handle search button click
        binding.btnSearch.setOnClickListener {
            // Gather input data
            val keyword = binding.etKeyword.text.toString()
            val category = categoryToSegmentId[binding.spinnerCategory.selectedItem.toString()]
            val distance = binding.etDistance.text.toString()
            if (distance == "") {
                Snackbar.make(binding.root, "Distance cannot be empty", Snackbar.LENGTH_LONG).show()
            }
            else {
                val distanceUnit = binding.spinnerDistanceUnit.selectedItem.toString()
                val currentDateTime = getCurrentDateTime()

                if (binding.rbSpecifyLocation.isChecked) {
                    specifyLocation(binding.etSpecifyLocation.text.toString(), keyword, category, distance, distanceUnit, currentDateTime)
                } else {
                    getCurrentLocation { lat, lng ->
                        val geoHash = geohash(lat, lng)
                        searchEvents(keyword, category, distance, distanceUnit, geoHash, currentDateTime)
                    }
                }
            }
        }

        // Handle clear button click
        binding.btnClear.setOnClickListener {
            binding.etKeyword.text.clear()
            binding.spinnerCategory.setSelection(0)
            binding.etDistance.text.clear()
            binding.spinnerDistanceUnit.setSelection(0)
            binding.rgLocation.check(R.id.rb_current_location)
            binding.etSpecifyLocation.text.clear()
        }
    }

    // Get the current date and time in the required format
    private fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return dateFormat.format(Date())
    }

    // Handle specifying a location
    private fun specifyLocation(location: String, keyword: String, category: String?, distance: String, distanceUnit: String, currentDateTime: String) {
        if (location == "") {
            Snackbar.make(binding.root, "Location cannot be empty", Snackbar.LENGTH_LONG).show()
        } else {
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses = geocoder.getFromLocationName(location, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                val address = addresses[0]
                val lat = address.latitude
                val lng = address.longitude
                val geoHash = geohash(lat, lng)
                searchEvents(keyword, category, distance, distanceUnit, geoHash, currentDateTime)
            } else {
                Snackbar.make(binding.root, "Invalid location", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    // Handle getting the current location
    private fun getCurrentLocation(callback: (Double, Double) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permissions
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    callback(location.latitude, location.longitude)
                } else {
                    Snackbar.make(binding.root, "Unable to get current location", Snackbar.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener {
                Snackbar.make(binding.root, "Unable to get current location", Snackbar.LENGTH_LONG).show()
            }
    }

    // Generate a GeoHash string from latitude and longitude
    private fun geohash(lat: Double, lng: Double): String {
        return GeoHash.geoHashStringWithCharacterPrecision(lat, lng, 9)
    }

    // Start the SearchResultsActivity with the provided search parameters
    private fun searchEvents(keyword: String, category: String?, distance: String, distanceUnit: String, geoHash: String, currentDateTime: String) {
        val intent = Intent(this, SearchResultsActivity::class.java).apply {
            putExtra("keyword", keyword)
            putExtra("category", category)
            putExtra("distance", distance)
            putExtra("distanceUnit", distanceUnit)
            putExtra("geoHash", geoHash)
            putExtra("currentDateTime", currentDateTime)
        }
        startActivity(intent)
    }

    // Handle the result of the location permission request
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getCurrentLocation { lat, lng ->
                    val geoHash = geohash(lat, lng)
                    // Gather input data
                    val keyword = binding.etKeyword.text.toString()
                    val category = categoryToSegmentId[binding.spinnerCategory.selectedItem.toString()]
                    val distance = binding.etDistance.text.toString()
                    val distanceUnit = binding.spinnerDistanceUnit.selectedItem.toString()
                    val currentDateTime = getCurrentDateTime()
                    searchEvents(keyword, category, distance, distanceUnit, geoHash, currentDateTime)
                }
            } else {
                Snackbar.make(binding.root, "Location permission denied", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
    }
}
