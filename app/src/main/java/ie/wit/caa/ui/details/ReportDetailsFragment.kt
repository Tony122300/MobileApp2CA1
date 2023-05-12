package ie.wit.caa.ui.details

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import ie.wit.caa.R
import ie.wit.caa.databinding.FragmentReportDetailsBinding
import ie.wit.caa.firebase.FirebaseDBManager.findByName
import ie.wit.caa.firebase.FirebaseDBManager.update
import ie.wit.caa.main.caaApp
//import ie.wit.caa.models.CaaManager
//import ie.wit.caa.models.CaaJSONStore
import ie.wit.caa.models.CaaModel
//import ie.wit.caa.models.Location
import ie.wit.caa.ui.auth.LoggedInViewModel
import ie.wit.caa.ui.reportCrimeActivity.ReportCrimeActivityViewModel

//import ie.wit.caa.models.CaaManager

class ReportDetailsFragment : Fragment() {

    private lateinit var name: EditText
    private lateinit var description: EditText
    private lateinit var type: EditText
    private lateinit var level: EditText
    private lateinit var date: EditText
    private lateinit var time: EditText

    private lateinit var viewModel: ReportDetailsViewModel
    private val args by navArgs<ReportDetailsFragmentArgs>()
    private lateinit var detailListener: ValueEventListener
    private lateinit var database: FirebaseDatabase
    private lateinit var detailRef: DatabaseReference
    private var _fragBinding: FragmentReportDetailsBinding? = null
    private val fragBinding get() = _fragBinding!!
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private val reportCrimeActivityViewModel : ReportCrimeActivityViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentReportDetailsBinding.inflate(inflater, container, false)
        val root = fragBinding.root
// initializing variables, finding the views for each in layout
        name = fragBinding.editName
        description = fragBinding.editDecription
        level = fragBinding.editDangerLvl
        type = fragBinding.editCrimeType
        time = fragBinding.editTime
        date = fragBinding.editDate

        viewModel = ViewModelProvider(this).get(ReportDetailsViewModel::class.java)
        viewModel.observableCaa.observe(viewLifecycleOwner, Observer { render() })

// delete button
        fragBinding.deleteReportButton.setOnClickListener {
            viewModel.delete(loggedInViewModel.liveFirebaseUser.value?.email!!,
                viewModel.observableCaa.value?.uid!!)
            findNavController().navigateUp()
        }

//edit buttion
        fragBinding.editReportButton.setOnClickListener {
            viewModel.updateCaa(loggedInViewModel.liveFirebaseUser.value?.uid!!,
                args.crimeid, fragBinding.caavm?.observableCaa!!.value!!)
            findNavController().navigateUp()
        }

        return root
    }
    //database reference to get data from database
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = FirebaseDatabase.getInstance("https://mobileapp2-371501-default-rtdb.europe-west1.firebasedatabase.app/")
        detailRef = database.getReference("crimes").child(FirebaseAuth.getInstance().currentUser!!.uid)
        detailListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val crime = snapshot.getValue(CaaModel::class.java)
                if (crime != null) {
                    Log.d("ReportDetailsFragment", "Retrieved crime: $crime")
                    name.setText(crime.name)
                    description.setText(crime.description)
                    type.setText(crime.type)
                    level.setText(crime.level.toString())
                    date.setText(crime.date)
                    time.setText(crime.time)

                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        }
        detailRef.addValueEventListener(detailListener)
    }

    private fun render() {
        fragBinding.caavm = viewModel
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCaa(loggedInViewModel.liveFirebaseUser.value?.uid!!,
            args.crimeid)

    }
    override fun onDestroyView() {
        super.onDestroyView()
        detailRef.removeEventListener(detailListener)
        _fragBinding = null
    }
}