package ie.wit.caa.ui.map

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.location.Location
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import ie.wit.caa.models.CaaModel

import timber.log.Timber

@SuppressLint("MissingPermission")
class MapsViewModel(application: Application) : AndroidViewModel(application) {
//declaring variables map,currentLocation,locationClient,onMapRendered
    lateinit var map : GoogleMap
    var currentLocation = MutableLiveData<Location>()
    var locationClient : FusedLocationProviderClient
    val context = getApplication<Application>().applicationContext
    var onMapRendered: (() -> Unit)? = null
    val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            //
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
//request location updates and retrieves it
        locationClient = LocationServices.getFusedLocationProviderClient(application)
        locationClient.requestLocationUpdates(locationRequest, locationCallback,
            Looper.getMainLooper())
    }
//updates location, gets last known location, default location set if cant get location
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

    private val notifiedCaaList = mutableListOf<CaaModel>()
// producing a toast for when the user enters the danger areas that the circle areas. does this by checking if the distance between user and crime is less that the radius created
    fun checkDangerAreas(caaList: List<CaaModel>, userLocation: Location) {
        val toastDuration = Toast.LENGTH_LONG
        caaList.forEach { caa ->
            val distance = FloatArray(1)
            Location.distanceBetween(
                userLocation.latitude,
                userLocation.longitude,
                caa.latitude,
                caa.longitude,
                distance
            )
            val dangerLevel = caa.level
            val dangerMessage = when (dangerLevel) {
                1 -> "In a level 1 very low danger area that has: ${caa.type} going on reported by: ${caa.name}"
                2 -> "In a level 2 low danger area that has: ${caa.type} going on reported by: ${caa.name}"
                3 -> "In a level 3 moderate danger area that has: ${caa.type} going on reported by: ${caa.name}"
                4 -> "In a level 4 high danger area that has: ${caa.type} going on reported by: ${caa.name}"
                5 -> "In a level 5 very high danger area that has: ${caa.type} going on reported by: ${caa.name}"
                6 -> "In a level 6 extremely high danger area that has: ${caa.type} going on reported by: ${caa.name}"
                7 -> "In a level 7 extreme danger area that has: ${caa.type} going on reported by: ${caa.name}"
                8 -> "In a level 8 severe danger area that has: ${caa.type} going on reported by: ${caa.name}"
                9 -> "In a level 9 very severe danger area that has: ${caa.type} going on reported by: ${caa.name}"
                else -> "In a level 10 extremely severe danger area that has: ${caa.type} going on reported by: ${caa.name}"
            }
            if (distance[0] <= dangerLevel * 100 && caa !in notifiedCaaList) {
                // Show toast message for danger area
                Toast.makeText(context, dangerMessage, toastDuration).show()
                notifiedCaaList.add(caa)
            } else if (distance[0] > dangerLevel * 100 && caa in notifiedCaaList) {
                // Remove from notified list if user exits danger area
                notifiedCaaList.remove(caa)
            }
        }
    }

}
