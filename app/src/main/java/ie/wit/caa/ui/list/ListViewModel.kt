package ie.wit.caa.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.caa.models.CaaJSONStore
//import ie.wit.caa.models.CaaManager
import ie.wit.caa.models.CaaModel
import ie.wit.caa.models.CaaStore

class ListViewModel : ViewModel() {
private val caaList = MutableLiveData<List<CaaModel>>()

    val observableCaaList: LiveData<List<CaaModel>>
    get() = caaList

    init {
        load()
    }

    fun load(){
        caaList.value = CaaJSONStore.findAll()
    }

    fun filterList(query: String) {
        val results = mutableListOf<CaaModel>()

        for (caa in CaaJSONStore.findAll()) {
            if (caa.type.contains(query, ignoreCase = true) ||
                caa.name.contains(query, ignoreCase = true)) {
                results.add(caa)
            }
        }


        caaList.value = results
    }
}