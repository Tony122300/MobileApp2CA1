package ie.wit.caa.ui.reportCrimeActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.caa.models.CaaManager
import ie.wit.caa.models.CaaModel

class ReportCrimeActivityViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    fun addCrime(caa: CaaModel) {
        status.value = try {
            CaaManager.create(caa)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}
