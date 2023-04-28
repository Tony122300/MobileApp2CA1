package ie.wit.caa.ui.reportCrimeActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.wit.caa.firebase.FirebaseDBManager
import ie.wit.caa.firebase.FirebaseImageManager
//import ie.wit.caa.firebase.FirebaseDBManager
//import ie.wit.caa.models.CaaManager
//import ie.wit.caa.models.CaaJSONStore
//import ie.wit.caa.models.CaaManager
//import ie.wit.caa.models.CaaManager
import ie.wit.caa.models.CaaModel
import timber.log.Timber

class ReportCrimeActivityViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

//    fun addCrime(caa: CaaModel) {
//        status.value = try {
//            CaaManager.create(caa)
//            true
//        } catch (e: IllegalArgumentException) {
//            false
//        }
//    }

    fun addCrime(firebaseUser: MutableLiveData<FirebaseUser>, caa: CaaModel) {
        Timber.i("Attempting to add crime: $caa")
        status.value = try {
            caa.profilepic = FirebaseImageManager.imageUri.value.toString()
            FirebaseDBManager.create(firebaseUser, caa)
            true
        } catch (e: IllegalArgumentException) {
            Timber.e(e, "Error adding crime: $caa")
            false
        }
    }


}
