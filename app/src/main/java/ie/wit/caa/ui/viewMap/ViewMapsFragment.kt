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
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.Marker
//import com.google.android.gms.maps.model.MarkerOptions
//import com.squareup.picasso.Picasso
//import ie.wit.caa.databinding.FragmentViewMapsBinding
//import ie.wit.caa.main.caaApp
////import ie.wit.caa.models.CaaManager
////import ie.wit.caa.models.CaaJSONStore
//import ie.wit.caa.models.CaaModel
//
//abstract class ViewMapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
//    private lateinit var binding: FragmentViewMapsBinding
//    lateinit var map: GoogleMap
//    lateinit var app: caaApp
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentViewMapsBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        app = activity?.application as caaApp
//        val mapFragment =
//            childFragmentManager.findFragmentById(binding.mapView.id) as SupportMapFragment
//        mapFragment.getMapAsync(this)
//    }
//
//    override fun onMapReady(googleMap: GoogleMap) {
//        map = googleMap
//        configureMap()
//    }
//
//    private fun configureMap() {
////        map.uiSettings.isZoomControlsEnabled = true
////        app.caas.findAll().forEach {
////            val loc = LatLng(it.lat, it.lng)
////            val options = MarkerOptions().title(it.name).position(loc)
////            map.addMarker(options)?.tag = it.id
////            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
////            map.setOnMarkerClickListener(this)
////        }
//    }
//
//    override fun onMarkerClick(marker: Marker): Boolean {
//        val foundCaa: CaaModel? = CaaManager.caas.find { it.id == marker.tag }
//        return false
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        val f = childFragmentManager.findFragmentById(binding.mapView.id)
//    }
//}
