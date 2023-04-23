package ie.wit.caa.ui.details

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
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
import ie.wit.caa.databinding.FragmentReportDetailsBinding
import ie.wit.caa.firebase.FirebaseDBManager.update
import ie.wit.caa.main.caaApp
//import ie.wit.caa.models.CaaManager
//import ie.wit.caa.models.CaaJSONStore
import ie.wit.caa.models.CaaModel
import ie.wit.caa.models.Location
import ie.wit.caa.ui.auth.LoggedInViewModel

//import ie.wit.caa.models.CaaManager

class ReportDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = ReportDetailsFragment()
    }

    private lateinit var viewModel: ReportDetailsViewModel
    private val args by navArgs<ReportDetailsFragmentArgs>()
    lateinit var app: caaApp

    private var _fragBinding: FragmentReportDetailsBinding? = null
    private val fragBinding get() = _fragBinding!!
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private val reportDetailsViewModel : ReportDetailsViewModel by activityViewModels()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentReportDetailsBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        viewModel = ViewModelProvider(this).get(ReportDetailsViewModel::class.java)
        viewModel.observableCaa.observe(viewLifecycleOwner, Observer { render() })
        fragBinding.deleteReportButton.setOnClickListener {
            viewModel.delete(loggedInViewModel.liveFirebaseUser.value?.email!!,
                viewModel.observableCaa.value?.uid!!)
            findNavController().navigateUp()
        }
        fragBinding.editReportButton.setOnClickListener {
            reportDetailsViewModel.updateDonation(loggedInViewModel.liveFirebaseUser.value?.uid!!,
                args.crimeid, fragBinding.caavm?.observableCaa!!.value!!)
            findNavController().navigateUp()
        }

        fragBinding.deleteReportButton.setOnClickListener {
            reportDetailsViewModel.delete(loggedInViewModel.liveFirebaseUser.value?.email!!,
                reportDetailsViewModel.observableCaa.value?.uid!!)
            findNavController().navigateUp()
        }
        return root
    }

    private fun render() {
        fragBinding.caavm = reportDetailsViewModel
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCaa(loggedInViewModel.liveFirebaseUser.value?.uid!!,
            args.crimeid)

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}