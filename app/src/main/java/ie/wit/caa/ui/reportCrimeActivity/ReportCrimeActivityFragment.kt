package ie.wit.caa.ui.reportCrimeActivity

import android.R
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import ie.wit.caa.databinding.FragmentReportCrimeActivityBinding
import ie.wit.caa.main.caaApp
import ie.wit.caa.models.CaaModel


class ReportCrimeActivityFragment : Fragment() {
    lateinit var app: caaApp
    private var _fragBinding: FragmentReportCrimeActivityBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var reportCrimeActivityViewModel: ReportCrimeActivityViewModel
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
        app = activity?.application as caaApp
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentReportCrimeActivityBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(ie.wit.caa.R.string.action_crime)

        fragBinding.amountPicker.minValue = 1
        fragBinding.amountPicker.maxValue = 10

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
    return root;
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    fun setButtonListener(layout: FragmentReportCrimeActivityBinding) {
        layout.addCrime.setOnClickListener {
            val name = fragBinding.FullName.text.toString()
            val description = fragBinding.Description.text.toString()

            val crimeType = fragBinding.spinner.selectedItem.toString()
            val crimeLevel = fragBinding.amountPicker.value
            if (name.isNotEmpty() && description.isNotEmpty()) {
                val crime = CaaModel(
                    name = name,
                    description = description,
                    type = crimeType,
                    level = crimeLevel
                )
               // app.crimeStore.create(crime)
                Snackbar.make(fragBinding.root, "Crime added successfully", Snackbar.LENGTH_LONG)
                    .show()
                fragBinding.FullName.setText("")
                fragBinding.Description.setText("")
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

}
