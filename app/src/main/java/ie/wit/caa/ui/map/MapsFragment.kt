package ie.wit.caa.ui.map


    import android.app.Activity
    import android.content.Intent
    import android.os.Bundle
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import androidx.fragment.app.Fragment
    import ie.wit.caa.databinding.FragmentMapsBinding
    import ie.wit.caa.models.Location
    import com.google.android.gms.maps.CameraUpdateFactory
    import com.google.android.gms.maps.GoogleMap
    import com.google.android.gms.maps.OnMapReadyCallback
    import com.google.android.gms.maps.SupportMapFragment
    import com.google.android.gms.maps.model.LatLng
    import com.google.android.gms.maps.model.Marker
    import com.google.android.gms.maps.model.MarkerOptions
    import ie.wit.caa.R
    import ie.wit.caa.main.caaApp

class MapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener {
        lateinit var app: caaApp
        private lateinit var map: GoogleMap
        private var _fragBinding: FragmentMapsBinding? = null
        private val fragBinding get() = _fragBinding!!
        private var location = Location()

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            _fragBinding = FragmentMapsBinding.inflate(inflater, container, false)
            return fragBinding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            location = requireArguments().getParcelable<ie.wit.caa.models.Location>("location")!!
            val mapsFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
           mapsFragment.getMapAsync(this)
        }

        override fun onMapReady(googleMap: GoogleMap) {
            map = googleMap
            val loc = LatLng(location.lat, location.lng)
            val options = MarkerOptions()
                .title("Crime Location")
                .snippet("GPS : $loc")
                .draggable(true)
                .position(loc)
            map.addMarker(options)
            map.setOnMarkerDragListener(this)
            map.setOnMarkerClickListener(this)
            map.uiSettings.isZoomControlsEnabled = true
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, location.zoom))
        }

        override fun onMarkerDragStart(marker: Marker) {
        }

        override fun onMarkerDrag(marker: Marker) {
        }

        override fun onMarkerDragEnd(marker: Marker) {
            location.lat = marker.position.latitude
            location.lng = marker.position.longitude
            location.zoom = map.cameraPosition.zoom
            //app.loc=location
        }


        override fun onMarkerClick(marker: Marker): Boolean {
            val loc = LatLng(location.lat, location.lng)
            marker.snippet = "GPS : $loc"
            return false
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _fragBinding = null
        }

    override fun onBackPressed() {
        val resultIntent = Intent()
        resultIntent.putExtra("location", location)
        requireActivity().setResult(Activity.RESULT_OK, resultIntent)
        requireActivity().finish()
        activity?.finish()
    }

}