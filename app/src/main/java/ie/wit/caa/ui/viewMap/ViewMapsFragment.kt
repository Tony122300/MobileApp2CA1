//package ie.wit.caa.ui.viewMap
//
//import androidx.fragment.app.Fragment
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.OnMapReadyCallback
//import com.google.android.gms.maps.SupportMapFragment
//import com.google.android.gms.maps.model.CameraPosition
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.MarkerOptions
//import ie.wit.caa.R
//import ie.wit.caa.databinding.FragmentViewMapsBinding
//import ie.wit.caa.main.caaApp
//
//class MapsViewFragment : Fragment(), OnMapReadyCallback {
//
//    private var _fragBinding: FragmentViewMapsBinding? = null
//    private val binding get() = _fragBinding!!
//
//    private lateinit var map: GoogleMap
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _fragBinding = FragmentViewMapsBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val mapsFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
//        mapsFragment.getMapAsync(this)
//    }
//
//    override fun onMapReady(googleMap: GoogleMap) {
//        map = googleMap
//
//        // add markers to the map
//        for (location in caaApp.locations) {
//            val marker = map.addMarker(
//                MarkerOptions()
//                    .position(LatLng(location.lat, location.lng))
//                    .title(location.title)
//            )
//
//            // set the marker's snippet to the location's description
//            if (marker != null) {
//                marker.snippet = location.description
//            }
//        }
//
//        // move the camera to the first location in the list
//        if (caaApp.locations.isNotEmpty()) {
//            val firstLocation = caaApp.locations.first()
//            val cameraPosition = CameraPosition.Builder()
//                .target(LatLng(firstLocation.lat, firstLocation.lng))
//                .zoom(firstLocation.zoom)
//                .build()
//            map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
//        }
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _fragBinding = null
//    }
//}
