import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ie.wit.caa.R
import ie.wit.caa.models.CaaModel
import ie.wit.caa.ui.auth.LoggedInViewModel
import ie.wit.caa.ui.list.ListViewModel
import ie.wit.caa.ui.map.MapsViewModel

//package ie.wit.caa.ui.map
//
//
//    import android.app.Activity
//    import android.content.Intent
//    import android.os.Bundle
//    import android.view.LayoutInflater
//    import android.view.View
//    import android.view.ViewGroup
//    import androidx.fragment.app.Fragment
//    import ie.wit.caa.databinding.FragmentMapsBinding
//    import ie.wit.caa.models.Location
//    import com.google.android.gms.maps.CameraUpdateFactory
//    import com.google.android.gms.maps.GoogleMap
//    import com.google.android.gms.maps.OnMapReadyCallback
//    import com.google.android.gms.maps.SupportMapFragment
//    import com.google.android.gms.maps.model.LatLng
//    import com.google.android.gms.maps.model.Marker
//    import com.google.android.gms.maps.model.MarkerOptions
//    import ie.wit.caa.R
//    import ie.wit.caa.main.caaApp
//
//class MapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener {
//        lateinit var app: caaApp
//        private lateinit var map: GoogleMap
//        private var _fragBinding: FragmentMapsBinding? = null
//        private val fragBinding get() = _fragBinding!!
//        private var location = Location()
//
//        override fun onCreateView(
//            inflater: LayoutInflater, container: ViewGroup?,
//            savedInstanceState: Bundle?
//        ): View {
//            _fragBinding = FragmentMapsBinding.inflate(inflater, container, false)
//            return fragBinding.root
//        }
//
//        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//            super.onViewCreated(view, savedInstanceState)
//
//            app = activity?.application as caaApp
//            location = requireArguments().getParcelable<ie.wit.caa.models.Location>("location")!!
//            val mapsFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
//           mapsFragment.getMapAsync(this)
//        }
//
//        override fun onMapReady(googleMap: GoogleMap) {
//            map = googleMap
//            val loc = LatLng(location.lat, location.lng)
//            val options = MarkerOptions()
//                .title("Crime Location")
//                .snippet("GPS : $loc")
//                .draggable(true)
//                .position(loc)
//            map.addMarker(options)
//            map.setOnMarkerDragListener(this)
//            map.setOnMarkerClickListener(this)
//            map.uiSettings.isZoomControlsEnabled = true
//            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, location.zoom))
//        }
//
//        override fun onMarkerDragStart(marker: Marker) {
//        }
//
//        override fun onMarkerDrag(marker: Marker) {
//        }
//
//        override fun onMarkerDragEnd(marker: Marker) {
//            location.lat = marker.position.latitude
//            location.lng = marker.position.longitude
//            location.zoom = map.cameraPosition.zoom
//            app.loc=location
//        }
//
//
//        override fun onMarkerClick(marker: Marker): Boolean {
//            val loc = LatLng(location.lat, location.lng)
//            marker.snippet = "GPS : $loc"
//            return false
//        }
//
//        override fun onDestroyView() {
//            super.onDestroyView()
//            _fragBinding = null
//        }
//
//    override fun onResume() {
//        super.onResume()
//        if(this::map.isInitialized) {
//            map.setOnMarkerClickListener(this)
//            map.setOnMarkerDragListener(this)
//        }
//    }
//
//    override fun onPause() {
//        super.onPause()
//        if(this::map.isInitialized) {
//            map.setOnMarkerClickListener(null)
//            map.setOnMarkerDragListener(null)
//        }
//    }
//
//}
class MapsFragment : Fragment() {
    private val listViewModel: ListViewModel by activityViewModels()
    private val mapsViewModel: MapsViewModel by activityViewModels()
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        mapsViewModel.map = googleMap
        mapsViewModel.map.isMyLocationEnabled = true
        mapsViewModel.currentLocation.observe(viewLifecycleOwner) {
            val loc = LatLng(
                mapsViewModel.currentLocation.value!!.latitude,
                mapsViewModel.currentLocation.value!!.longitude
            )

            mapsViewModel.map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 14f))
            mapsViewModel.map.uiSettings.isZoomControlsEnabled = true
            mapsViewModel.map.uiSettings.isMyLocationButtonEnabled = true
            listViewModel.observableCaaList.observe(
                viewLifecycleOwner,
                Observer { caas ->
                    caas?.let {
                        render(caas as ArrayList<CaaModel>)

                    }
                })

        }
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupMenu()
        return inflater.inflate(R.layout.fragment_maps2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun render(caaList: ArrayList<CaaModel>) {
        val levelColors = arrayOf(
            Color.argb(70, 0, 255, 0), // level 1 (green)
            Color.argb(70, 128, 255, 0), // level 2
            Color.argb(70, 255, 255, 0), // level 3
            Color.argb(70, 255, 128, 0), // level 4
            Color.argb(70, 255, 0, 0), // level 5
            Color.argb(70, 255, 0, 128), // level 6
            Color.argb(70, 128, 0, 255), // level 7
            Color.argb(70, 255, 0, 255),// level 8
            Color.argb(70, 225, 0, 0), // level 9 red
            Color.argb(70, 150, 0, 55) // level 10 dark cherry red
        )
        var markerColour: Float
        if (caaList.isNotEmpty()) {
            mapsViewModel.map.clear()
            caaList.forEach {
                markerColour =
                    if (it.email.equals(this.listViewModel.liveFirebaseUser.value!!.email))
                        BitmapDescriptorFactory.HUE_AZURE + 5
                    else
                        BitmapDescriptorFactory.HUE_RED
                val location = LatLng(it.latitude, it.longitude)
                val level = it.level.coerceIn(1, 10) - 1 // index into levelColors array
                val radiusColor = levelColors[level]
                mapsViewModel.map.addMarker(
                    MarkerOptions().position(LatLng(it.latitude, it.longitude))
                        .title("Lvl${it.level}! - ${it.type}")
                        .snippet(it.name)
                        .icon(BitmapDescriptorFactory.defaultMarker(markerColour))
                )
                mapsViewModel.map.addCircle(
                    CircleOptions()
                        .center(location)
                        .radius(it.level * 100.0) // radius is in meters
                        .strokeColor(Color.RED)
                        .fillColor(radiusColor)
                )
            }
            mapsViewModel.checkDangerAreas(caaList, mapsViewModel.currentLocation.value!!)
        }
    }


    override fun onResume() {
        super.onResume()
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner) {
                firebaseUser -> if (firebaseUser != null) {
            listViewModel.liveFirebaseUser.value = firebaseUser
            listViewModel.load()
        }        }
    }
    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_list, menu)

                val item = menu.findItem(R.id.toggleCrimes) as MenuItem
                item.setActionView(R.layout.togglebutton_layout)
                val toggleCaas: SwitchCompat = item.actionView!!.findViewById(R.id.toggleButton)
                toggleCaas.isChecked = false

                toggleCaas.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) listViewModel.loadAll()
                    else listViewModel.load()
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }     }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

}

