package ie.wit.caa.ui.details

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import ie.wit.caa.R

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
        Toast.makeText(context,"erer:${args.crimeid}",Toast.LENGTH_LONG).show()
        return view
    }

}