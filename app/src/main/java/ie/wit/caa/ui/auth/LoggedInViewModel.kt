package ie.wit.caa.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import ie.wit.caa.firebase.FirebaseAuthManager

class LoggedInViewModel(app: Application) : AndroidViewModel(app) {

    var liveFirebaseUser = MutableLiveData<FirebaseUser>()
    var loggedOut = MutableLiveData<Boolean>()
    var errorStatus = MutableLiveData<Boolean>()

    fun signOut() {
        loggedOut.postValue(true)
        errorStatus.postValue(false)
    }
}
