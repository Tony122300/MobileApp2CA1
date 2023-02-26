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
import ie.wit.caa.main.caaApp
import ie.wit.caa.models.CaaJSONStore
import ie.wit.caa.models.CaaModel
import ie.wit.caa.models.Location

//import ie.wit.caa.models.CaaManager

class ReportDetailsFragment : Fragment(){//, GoogleMap.OnMarkerClickListener, OnMapReadyCallback {

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

        view.findViewById<TextView>(R.id.editCrimeType).text = caa?.type.toString()
        view.findViewById<TextView>(R.id.editDecription).text = caa?.description
        view.findViewById<TextView>(R.id.editDangerLvl).text = caa?.level.toString()
        view.findViewById<TextView>(R.id.editName).text = caa?.name
        view.findViewById<TextView>(R.id.editDate).text = caa?.date
        view.findViewById<TextView>(R.id.editTime).text = caa?.time
        Toast.makeText(context, "erer:${args.crimeid}", Toast.LENGTH_LONG).show()

        view.findViewById<Button>(R.id.deleteReportButton).setOnClickListener {
            viewModel.delete(caa!!)
            findNavController().navigate(R.id.action_reportDetailsFragment_to_listFragment)
        }
        view.findViewById<Button>(R.id.editReportButton).setOnClickListener {
            val type = view.findViewById<TextView>(R.id.editCrimeType).text.toString()
            val description = view.findViewById<TextView>(R.id.editDecription).text.toString()
            val level = view.findViewById<TextView>(R.id.editDangerLvl).text.toString().toIntOrNull() ?: 0
            val name = view.findViewById<TextView>(R.id.editName).text.toString()
            val date = view.findViewById<TextView>(R.id.editDate).text.toString()
            val time = view.findViewById<TextView>(R.id.editTime).text.toString()
            if (name.isNotEmpty() && description.isNotEmpty() && level != null && type.isNotEmpty() && date.isNotEmpty() && time.isNotEmpty()) {
                caa?.name = name
                caa?.description = description
                caa?.level = level
                caa?.type = type
                caa?.date = date
                caa?.time = time
                CaaJSONStore.update(caa!!)
                Toast.makeText(context, "Report updated", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_reportDetailsFragment_to_listFragment)
            } else {
                Toast.makeText(context, "Please fill all the details", Toast.LENGTH_LONG).show()
            }
        }
        return view
    }


//        view.findViewById<Button>(R.id.deleteReportButton).setOnClickListener {
//            showConfirmationDialog {
//                viewModel.delete(caa!!)
//                findNavController().navigate(R.id.action_reportDetailsFragment_to_listFragment)
//            }
//        }
//
//        view.findViewById<Button>(R.id.editReportButton).setOnClickListener {
//            showConfirmationDialog {
//                val type = view.findViewById<TextView>(R.id.editCrimeType).text.toString()
//                val description = view.findViewById<TextView>(R.id.editDecription).text.toString()
//                val level =
//                    view.findViewById<TextView>(R.id.editDangerLvl).text.toString().toIntOrNull()
//                        ?: 0
//                val name = view.findViewById<TextView>(R.id.editName).text.toString()
//                val date = view.findViewById<TextView>(R.id.editDate).text.toString()
//                val time = view.findViewById<TextView>(R.id.editTime).text.toString()
//                if (name.isNotEmpty() && description.isNotEmpty() && level != null && type.isNotEmpty() && date.isNotEmpty() && time.isNotEmpty()) {
//                    caa?.name = name
//                    caa?.description = description
//                    caa?.level = level
//                    caa?.type = type
//                    caa?.date = date
//                    caa?.time = time
//                    CaaJSONStore.update(caa!!)
//                    Toast.makeText(context, "Report updated", Toast.LENGTH_LONG).show()
//                    findNavController().navigate(R.id.action_reportDetailsFragment_to_listFragment)
//                } else {
//                    Toast.makeText(context, "Please fill all the details", Toast.LENGTH_LONG).show()
//                }
//            }
//        }
//            return view
//        }
//    private fun showConfirmationDialog(action: () -> Unit) {
//        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_report_details, null)
//        val dialog = AlertDialog.Builder(requireContext())
//            .setView(dialogView)
//            .create()
//
//        dialogView.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
//            dialog.dismiss()
//        }
//
//        dialogView.findViewById<Button>(R.id.btn_save).setOnClickListener {
//            action.invoke()
//            dialog.dismiss()
//        }
//
//        dialog.show()
//    }


}