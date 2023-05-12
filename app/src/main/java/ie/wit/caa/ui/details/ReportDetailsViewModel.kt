package ie.wit.caa.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.caa.firebase.FirebaseDBManager
//import ie.wit.caa.models.CaaJSONStore
//import ie.wit.caa.models.CaaManager
import ie.wit.caa.models.CaaModel
import timber.log.Timber

//import ie.wit.caa.models.CaaManager

class ReportDetailsViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()
    private val caa = MutableLiveData<CaaModel>()
    var observableCaa: LiveData<CaaModel>
        get() = caa
        set(value) {caa.value = value.value}

//    fun delete(caa: CaaModel) {
//        status.value = try {
//            CaaManager.delete(caa)
//            true
//        } catch (e: IllegalArgumentException) {
//            false
//        }
//    }
fun delete(userid: String, id: String) {
    try {
        FirebaseDBManager.delete(userid,id)
        Timber.i("Report Delete Success")
    }
    catch (e: Exception) {
        Timber.i("Report Delete Error : $e.message")
    }
}

//    fun update(caa: CaaModel){
//        status.value = try {
//            CaaManager.update(caa)
//            true
//        } catch (e: IllegalArgumentException) {
//            false
//        }
//    }
fun getCaa(userid:String, id: String) {
    try {
        FirebaseDBManager.findById(userid, id, caa)
        Timber.i("Detail getCaa() Success : ${
            caa.value.toString()}")
    }
    catch (e: Exception) {
        Timber.i("Detail getCaa() Error : $e.message")
    }
}
    fun updateCaa(userid:String, id: String,caa: CaaModel) {
        try {
            FirebaseDBManager.update(userid, id, caa)
            Timber.i("Detail update() Success : $caa")
        }
        catch (e: Exception) {
            Timber.i("Detail update() Error : $e.message")
        }
    }
}