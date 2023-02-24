package ie.wit.caa.ui.reportCrimeActivity

import android.R
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import ie.wit.caa.databinding.FragmentReportCrimeActivityBinding
import ie.wit.caa.models.CaaModel
import ie.wit.caa.models.Location
import timber.log.Timber.i
import java.util.*


class ReportCrimeActivityFragment : Fragment() {

    private lateinit var caaApp: CaaModel
    private var _fragBinding: FragmentReportCrimeActivityBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var reportCrimeActivityViewModel: ReportCrimeActivityViewModel
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private val choices = arrayOf(
        "Drug",
        "Cyber",
        "Property",
        "Violent",
        "Traffic",
        "Workplace",
        "Sports",
        "Home",
        "Natural"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        caaApp = CaaModel()
        //app = activity?.application as caaApp
        setHasOptionsMenu(true)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentReportCrimeActivityBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(ie.wit.caa.R.string.action_crime)
        reportCrimeActivityViewModel =
            ViewModelProvider(this).get(ReportCrimeActivityViewModel::class.java)
        reportCrimeActivityViewModel.observableStatus.observe(
            viewLifecycleOwner,
            Observer { status ->
                status?.let { render(status) }
            })

        fragBinding.amountPicker.minValue = 1
        fragBinding.amountPicker.maxValue = 10

        val datePicker = view?.findViewById<DatePicker>(ie.wit.caa.R.id.date_Picker)
        val today = Calendar.getInstance()
        if (datePicker != null) {
            datePicker.init(
                today.get(Calendar.YEAR), today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH)
            ) { _, year, month, dayOfMonth ->
                val month = month + 1
                val msg = "You Selected: $dayOfMonth/$month/$year"
                Toast.makeText(requireContext(), "Selected date: $msg", Toast.LENGTH_SHORT).show()
            }
        }

        val timePicker = fragBinding.timePicker
        timePicker.setOnTimeChangedListener { _, hour, minute ->
            var hour = hour
            var am_pm = ""
            // AM_PM decider logic
            when {
                hour == 0 -> {
                    hour += 12
                    am_pm = "AM"
                }
                hour == 12 -> am_pm = "PM"
                hour > 12 -> {
                    hour -= 12
                    am_pm = "PM"
                }
                else -> am_pm = "AM"
            }
            // Use the formatted time here
            val formattedTime = String.format("%02d:%02d %s", hour, minute, am_pm)
            Toast.makeText(requireContext(), "Selected time: $formattedTime", Toast.LENGTH_SHORT).show()
        }

        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, choices)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        fragBinding.spinner.adapter = adapter
        fragBinding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
            }


            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
       setButtonListener(fragBinding)
        setMapButtonListener(fragBinding)
        return root
    }

    fun setMapButtonListener(layout: FragmentReportCrimeActivityBinding) {
        layout.crimeLocation.setOnClickListener {
            val location1 = Location(52.245696, -7.139102, 15f)
            if (caaApp.zoom != 0f) {
                location1.lat = caaApp.lat
                location1.lng = caaApp.lng
                location1.zoom = caaApp.zoom
            }
                val action = ReportCrimeActivityFragmentDirections.actionReportCrimeActivityFragmentToMapsFragment(location1)
                findNavController().navigate(action)
        }
        registerMapCallback()
    }


    private fun render(status: Boolean) {
        when (status) {
            true -> {
                view?.let {
                    //Uncomment this if you want to immediately return to Report
                    //findNavController().popBackStack()
                }
            }
            false -> Toast.makeText(context,getString(ie.wit.caa.R.string.cant_upload_crime),Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }



    fun setButtonListener(layout: FragmentReportCrimeActivityBinding) {
        layout.addCrime.setOnClickListener {
            val datePicker = fragBinding.datePicker
            val timePicker = fragBinding.timePicker
            val name = fragBinding.FullName.text.toString()
            val description = fragBinding.Description.text.toString()
            val crimeType = fragBinding.spinner.selectedItem.toString()
            val crimeLevel = fragBinding.amountPicker.value
            val date = "${datePicker.dayOfMonth}/${datePicker.month + 1}/${datePicker.year}"
            val time = "${timePicker.hour}:${timePicker.minute}"
            if (name.isNotEmpty() && description.isNotEmpty()) {
                val crime = CaaModel(
                    name = name,
                    description = description,
                    type = crimeType,
                    level = crimeLevel,
                    date = date,
                    time = time
                )
               // app.crimeStore.create(crime)
                Snackbar.make(fragBinding.root, "Crime added successfully", Snackbar.LENGTH_LONG)
                    .show()
                fragBinding.FullName.setText("")
                fragBinding.Description.setText("")
                reportCrimeActivityViewModel.addCrime(crime)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please enter both name and description of the crime",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
}

    override fun onResume() {
        super.onResume()
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(ie.wit.caa.R.menu.menu_caa, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,
            requireView().findNavController()) || super.onOptionsItemSelected(item)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ReportCrimeActivityFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            caaApp.lat = location.lat
                            caaApp.lng = location.lng
                            caaApp.zoom = location.zoom
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }


}
