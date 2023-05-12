package ie.wit.caa.ui.list

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ie.wit.caa.R
import ie.wit.caa.adapter.CrimeAdapter
import ie.wit.caa.adapter.ReportClickListener
import ie.wit.caa.databinding.FragmentListBinding

import ie.wit.caa.main.caaApp
import ie.wit.caa.models.CaaModel
import ie.wit.caa.ui.auth.LoggedInViewModel
import ie.wit.caa.utils.SwipeToDeleteCallback
import ie.wit.caa.utils.SwipeToEditCallback

class ListFragment : Fragment(), ReportClickListener {
    lateinit var app: caaApp
    private var _fragBinding: FragmentListBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var listViewModel: ListViewModel
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()

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
        setupMenu()
        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)

        listViewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        listViewModel.observableCaaList.observe(viewLifecycleOwner, Observer {
                caa ->
            caa?.let { render(caa as ArrayList<CaaModel>) }
        })


        val fab: FloatingActionButton = fragBinding.fab
        fab.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToReportCrimeActivityFragment()
            findNavController().navigate(action)
        }

        setSwipeRefresh()

        val swipeDeleteHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = fragBinding.recyclerView.adapter as CrimeAdapter
                adapter.removeAt(viewHolder.adapterPosition)
                listViewModel.delete(listViewModel.liveFirebaseUser.value?.uid!!,
                    (viewHolder.itemView.tag as CaaModel).uid!!)

            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(fragBinding.recyclerView)

        val swipeEditHandler = object : SwipeToEditCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onReportClick(viewHolder.itemView.tag as CaaModel)
            }
        }
        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchEditHelper.attachToRecyclerView(fragBinding.recyclerView)

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

    }
    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {

            }
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_list, menu)

                val item = menu.findItem(R.id.toggleCrimes) as MenuItem
                item.setActionView(R.layout.togglebutton_layout)
                val toggleCrimes: SwitchCompat =
                    item.actionView!!.findViewById(R.id.toggleButton)
                toggleCrimes.isChecked = false

                toggleCrimes.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) listViewModel.loadAll()
                    else listViewModel.load()
                }
            }
            override fun onMenuItemSelected(item: MenuItem): Boolean {
            return NavigationUI.onNavDestinationSelected(
                item, requireView().findNavController())
        }
    }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


    private fun render(caaList: ArrayList<CaaModel>) {
        fragBinding.recyclerView.adapter = CrimeAdapter(caaList,this,listViewModel.readOnly.value!!)
        listViewModel.readOnly.value!!
        if (caaList.isEmpty()) {
            fragBinding.recyclerView.visibility = View.GONE
            fragBinding.noCrimes.visibility = View.VISIBLE
        } else {
            fragBinding.recyclerView.visibility = View.VISIBLE
            fragBinding.noCrimes.visibility = View.GONE
        }
    }
    @SuppressLint("SuspiciousIndentation")
    private fun setSwipeRefresh() {
        fragBinding.swiperefresh.setOnRefreshListener {
            fragBinding.swiperefresh.isRefreshing = true
            if(listViewModel.readOnly.value!!)
                listViewModel.loadAll()
            else
            listViewModel.load()
        }
    }
    override fun onReportClick(caa: CaaModel) {
        val action = ListFragmentDirections.actionListFragmentToReportDetailsFragment(caa.uid!!)
       if(!listViewModel.readOnly.value!!)
        findNavController().navigate(action)
    }
    override fun onResume() {
        super.onResume()
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner, Observer { firebaseUser ->
            if (firebaseUser != null) {
                listViewModel.liveFirebaseUser.value = firebaseUser
                listViewModel.load()
            }
        })
    }
}
