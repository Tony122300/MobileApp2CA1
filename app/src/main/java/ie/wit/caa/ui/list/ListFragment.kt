package ie.wit.caa.ui.list

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ie.wit.caa.R
import ie.wit.caa.adapter.CrimeAdapter
import ie.wit.caa.adapter.ReportClickListener
import ie.wit.caa.databinding.FragmentListBinding

import ie.wit.caa.main.caaApp
import ie.wit.caa.models.CaaModel

class ListFragment : Fragment(), ReportClickListener {
    lateinit var app: caaApp
    private var _fragBinding: FragmentListBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var listViewModel: ListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as caaApp
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentListBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)

        listViewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        listViewModel.observableCaaList.observe(viewLifecycleOwner, Observer {
                caa ->
            caa?.let { render(caa) }
        })

        val fab: FloatingActionButton = fragBinding.fab
        fab.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToReportCrimeActivityFragment()
            findNavController().navigate(action)
        }
        return root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ListFragment().apply {
                arguments = Bundle().apply { }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchView = view.findViewById<SearchView>(R.id.searchView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Do nothing when the user submits the search query
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Update the filter and refresh the adapter when the user types a new search query
                newText?.let { listViewModel.filterList(it) }
                return true
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,
            requireView().findNavController()) || super.onOptionsItemSelected(item)
    }


    private fun render(caaList: List<CaaModel>) {
        fragBinding.recyclerView.adapter = CrimeAdapter(caaList,this)
        if (caaList.isEmpty()) {
            fragBinding.recyclerView.visibility = View.GONE
            fragBinding.noCrimes.visibility = View.VISIBLE
        } else {
            fragBinding.recyclerView.visibility = View.VISIBLE
            fragBinding.noCrimes.visibility = View.GONE
        }
    }
    override fun onReportClick(caa: CaaModel) {
        val action = ListFragmentDirections.actionListFragmentToReportDetailsFragment(caa.id)
       findNavController().navigate(action)
    }
    override fun onResume() {
        super.onResume()
        listViewModel.load()
    }
}
