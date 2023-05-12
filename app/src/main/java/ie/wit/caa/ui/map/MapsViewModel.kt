package ie.wit.caa.ui.map

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import ie.wit.caa.models.CaaModel

import timber.log.Timber

@SuppressLint("MissingPermission")
class MapsViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var map : GoogleMap
    var currentLocation = MutableLiveData<Location>()
    var locationClient : FusedLocationProviderClient
    val context = getApplication<Application>().applicationContext
    var onMapRendered: (() -> Unit)? = null
    val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
        .setWaitForAccurateLocation(false)
        .setMinUpdateIntervalMillis(5000)
        .setMaxUpdateDelayMillis(15000)
        .build()

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            currentLocation.value = locationResult.locations.last()
        }
    }

    init {

        locationClient = LocationServices.getFusedLocationProviderClient(application)
        locationClient.requestLocationUpdates(locationRequest, locationCallback,
            Looper.getMainLooper())
    }

    fun updateCurrentLocation() {
        if(locationClient.lastLocation.isSuccessful)
            locationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    currentLocation.value = location!!
                    Timber.i("MAP VM LOC SUCCESS: %s", currentLocation.value)
                }
        else // Couldn't get Last Location
            currentLocation.value = Location("Default").apply {
                latitude = 52.245696
                longitude = -7.139102
            }
        Timber.i("MAP VM LOC : %s", currentLocation.value)
    }

    fun checkDangerAreas(caaList: List<CaaModel>, userLocation: Location) {
        onMapRendered = {
            caaList.forEach { caa ->
                val distance = FloatArray(1)
                Location.distanceBetween(
                    userLocation.latitude,
                    userLocation.longitude,
                    caa.latitude,
                    caa.longitude,
                    distance
                )
                if (distance[0] <= caa.level * 100) {
                    // Show popup message for danger area
                    val alertDialog = AlertDialog.Builder(context)
                        .setTitle("Danger Area Alert")
                        .setMessage("You are in a dangerous area: ${caa.name}")
                        .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                        .create()

                    alertDialog.show()
                }
            }
        }
    }


}
