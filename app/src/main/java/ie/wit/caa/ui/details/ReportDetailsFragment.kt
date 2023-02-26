package ie.wit.caa.ui.details

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ie.wit.caa.R
import ie.wit.caa.main.caaApp
import ie.wit.caa.models.CaaJSONStore
import ie.wit.caa.models.CaaModel
import ie.wit.caa.models.Location

//import ie.wit.caa.models.CaaManager

class ReportDetailsFragment : Fragment(){ //GoogleMap.OnMarkerClickListener, OnMapReadyCallback {

    companion object {
        fun newInstance() = ReportDetailsFragment()
    }

    private lateinit var viewModel: ReportDetailsViewModel
    private val args by navArgs<ReportDetailsFragmentArgs>()
    lateinit var map: GoogleMap
    private lateinit var mapView: MapView
    lateinit var app: caaApp

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_report_details, container, false)
        viewModel = ViewModelProvider(this).get(ReportDetailsViewModel::class.java)

        // val caaDetailsTextView = view.findViewById<TextView>(R.id.caaDetailsTextView)


        val caaId = args.crimeid
        val caa = CaaJSONStore.findById(caaId)

        view.findViewById<TextView>(R.id.editCrimeType).text = caa?.type
        view.findViewById<TextView>(R.id.editDecription).text = caa?.description
        view.findViewById<TextView>(R.id.editDangerLvl).text = caa?.level.toString()
        view.findViewById<TextView>(R.id.editName).text = caa?.name
        view.findViewById<TextView>(R.id.editDate).text = caa?.date
        view.findViewById<TextView>(R.id.editTime).text = caa?.time
        Toast.makeText(context, "erer:${args.crimeid}", Toast.LENGTH_LONG).show()

//        mapView = view.findViewById(R.id.mapViews)
//        mapView.onCreate(savedInstanceState)
//        mapView.onResume()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        // Get the GoogleMap object from the MapView
//        mapView.getMapAsync(this)
    }

//    override fun onMapReady(googleMap: GoogleMap) {
//        map = googleMap
//        configureMap()
//    }
//
//    private fun configureMap() {
//        map.uiSettings.isZoomControlsEnabled = true
//        app.caas.findAll().forEach {
//            val loc = LatLng(it.lat, it.lng)
//            val options = MarkerOptions().title(it.name).position(loc)
//            map.addMarker(options)?.tag = it.id
//            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
//            map.setOnMarkerClickListener(this)
//        }
//    }
//
//    override fun onMarkerClick(marker: Marker): Boolean {
//        val foundCaa: CaaModel? = CaaJSONStore.caas.find { it.id == marker.tag }
//        return false
//    }
//
//    override fun onResume() {
//        super.onResume()
//        if (this::map.isInitialized) {
//            map.setOnMarkerClickListener(this)
//        }
//    }
//
//    override fun onPause() {
//        super.onPause()
//        if (this::map.isInitialized) {
//            map.setOnMarkerClickListener(null)
//        }
//
//    }
}