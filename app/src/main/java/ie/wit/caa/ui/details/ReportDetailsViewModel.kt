package ie.wit.caa.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.caa.models.CaaJSONStore
import ie.wit.caa.models.CaaModel

//import ie.wit.caa.models.CaaManager

class ReportDetailsViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    fun delete(caa: CaaModel) {
        status.value = try {
            CaaJSONStore.delete(caa)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    fun update(caa: CaaModel){
        status.value = try {
            CaaJSONStore.update(caa)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}