package ie.wit.caa.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.wit.caa.firebase.FirebaseDBManager
//import ie.wit.caa.firebase.FirebaseDBManager
//import ie.wit.caa.models.CaaManager
//import ie.wit.caa.models.CaaManager
//import ie.wit.caa.models.CaaManager
import ie.wit.caa.models.CaaModel
import ie.wit.caa.models.CaaStore
import timber.log.Timber
import java.lang.Exception

class ListViewModel : ViewModel() {
private val caaList = MutableLiveData<List<CaaModel>>()
    var readOnly = MutableLiveData(false)
    var liveFirebaseUser = MutableLiveData<FirebaseUser>()
    val observableCaaList: LiveData<List<CaaModel>>
    get() = caaList

    init {
        load()
    }

//    fun load() {
//        try {
//            caaList.value = CaaManager.findAll()
//            Timber.i("Retrofit Success : ${caaList.value}")
//        } catch (e: Exception) {
//            Timber.i("Retrofit Error : ${e.message}")
//        }
//    }
fun load() {
    try {
        readOnly.value = false
        FirebaseDBManager.findAll(liveFirebaseUser.value?.uid!!,caaList)
        Timber.i("Report Load Success : ${caaList.value.toString()}")
    }
    catch (e: Exception) {
        Timber.i("Report Load Error : $e.message")
    }
}
    fun loadAll() {
        try {
            readOnly.value = true
            FirebaseDBManager.findAllInAll(caaList)
            Timber.i("Crime LoadAll Success : ${caaList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Crime LoadAll Error : $e.message")
        }
    }

    fun delete(userid: String, id: String) {
        try {
            FirebaseDBManager.delete(userid,id)
            Timber.i("Report Delete Success")
        }
        catch (e: Exception) {
            Timber.i("Report Delete Error : $e.message")
        }
    }


//
//    fun filterList(query: String) {
//        val results = mutableListOf<CaaModel>()
//
//        for (caa in CaaManager.findAll()) {
//            if (caa.type.contains(query, ignoreCase = true) ||
//                caa.name.contains(query, ignoreCase = true)) {
//                results.add(caa)
//            }
//        }
//
//
//        caaList.value = results
//    }
}

