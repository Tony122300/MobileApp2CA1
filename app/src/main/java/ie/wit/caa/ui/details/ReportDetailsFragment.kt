package ie.wit.caa.ui.details

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
import ie.wit.caa.R
import ie.wit.caa.models.CaaJSONStore
//import ie.wit.caa.models.CaaManager

class ReportDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = ReportDetailsFragment()
    }

    private lateinit var viewModel: ReportDetailsViewModel
    private val args by navArgs<ReportDetailsFragmentArgs>()

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
        Toast.makeText(context,"erer:${args.crimeid}",Toast.LENGTH_LONG).show()
        return view
    }

}