package pt.ipbeja.pi.piproject.identificationInsect

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch
import pt.ipbeja.pi.piproject.R
import pt.ipbeja.pi.piproject.persistence.MyIdentificationsDb
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Activity for displaying and selecting location on a map.
 */
class MapsActivity : FragmentActivity(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    private var save: Button? = null
    private var counter = 1

    private var markerFixed: Marker? = null
    private var markerNew: Marker? = null
    private var order: String? = null
    private var resultid2: String? = null
    private var picture2: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Check for Google Play Services
        val status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(applicationContext)
        if (status == ConnectionResult.SUCCESS) {
            val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            mapFragment?.getMapAsync(this)
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 10)?.show()
        }

        save = findViewById(R.id.savebtn)
        save?.setOnClickListener {
            val intent = Intent(this@MapsActivity, SaveIdentification::class.java).apply {
                putExtra("picturefinal", picture2)
                putExtra("resultidfinal", resultid2)
                putExtra("ordemfinal", order)
                markerNew?.let {
                    putExtra("novaLatitude", it.position.latitude)
                    putExtra("novaLongitude", it.position.longitude)
                }
                putExtra("extra1", 1)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            startActivity(intent)
        }

        // Check if showing all identifications
        val showAll = intent.getBooleanExtra("showAll", false)
        if (showAll) {
            save?.visibility = View.GONE // Hide save button when viewing all
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        picture2 = intent.getParcelableExtra("picture")
        resultid2 = intent.getStringExtra("resultid")
        order = intent.getStringExtra("ordem")

        mMap = googleMap.apply {
            mapType = GoogleMap.MAP_TYPE_SATELLITE
            uiSettings.isZoomControlsEnabled = true
        }

        val showAll = intent.getBooleanExtra("showAll", false)
        if (showAll) {
            // When showing all identifications, use normal map type for better visibility
            mMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
            // When showing all identifications, don't set up single location mode
            mMap?.setOnMapClickListener(null) // Disable click listener for selecting new location
            loadAllIdentifications()
        } else {
            val latitude = intent.getDoubleExtra("latitude2", 0.0)
            val longitude = intent.getDoubleExtra("longitude2", 0.0)
            val beja = LatLng(latitude, longitude)

            markerFixed = mMap?.addMarker(MarkerOptions().position(beja).title("Localização Foto!"))
            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(beja, 16f))

            mMap?.setOnMapClickListener { latLng ->
                markerNew?.remove()
                mMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
                markerNew = mMap?.addMarker(
                    MarkerOptions()
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                        .position(latLng)
                        .title("${latLng.latitude} : ${latLng.longitude}")
                )
            }
        }
    }

    fun onClickchangeView(view: android.view.View?) {
        mMap?.let {
            if (counter % 2 == 0) {
                it.mapType = GoogleMap.MAP_TYPE_SATELLITE
            } else {
                it.mapType = GoogleMap.MAP_TYPE_NORMAL
            }
            counter++
        }
    }

    fun onClickBack(view: android.view.View?) {
        startActivity(Intent(this@MapsActivity, SaveIdentification::class.java))
    }

    private fun loadAllIdentifications() {
        // Logic to load and display all saved identifications on the map
        lifecycleScope.launch {
            // Access the database and load identifications
            val db = MyIdentificationsDb.getDatabase(applicationContext)
            val identifications = db.identificationDao().getAll()

            if (identifications.isNotEmpty()) {
                // Define bounds to include all markers
                val builder = LatLngBounds.builder()

                // Add a marker for each identification
                for (identification in identifications) {
                    val latLng = LatLng(identification.latitude, identification.longitude)
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    val dateStr = dateFormat.format(identification.timestamp)
                    mMap?.addMarker(
                        MarkerOptions()
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                            .position(latLng)
                            .title("${identification.order} - $dateStr")
                    )
                    // Include this position in the bounds
                    builder.include(latLng)
                }

                // Adjust the camera to show all markers
                val bounds = builder.build()
                mMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
            } else {
                // If no identifications, show a default location (Portugal/Beja area)
                val defaultLocation = LatLng(38.0147, -7.8632) // Approximate coordinates for Beja, Portugal
                mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10f))
            }
        }
    }
}