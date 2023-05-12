package ie.wit.caa.ui.auth


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import ie.wit.caa.R
import ie.wit.caa.activities.Home
import ie.wit.caa.databinding.LoginBinding
import ie.wit.caa.firebaseui.FirebaseUIAuthManager

import timber.log.Timber
import timber.log.Timber.i

class Login : AppCompatActivity() {

    private lateinit var signIn: ActivityResultLauncher<Intent>

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
// checks if user is signed in
        registerFirebaseAuthUICallback()
        if(FirebaseUIAuthManager.isSignedIn())
            startActivity(Intent(this, Home::class.java))
        else
            signIn.launch(
                FirebaseUIAuthManager
                .createAndLaunchSignInIntent(R.layout.login))
    }
// setting up authentication callback when users signin
    private fun registerFirebaseAuthUICallback() {
        signIn = registerForActivityResult(
            FirebaseAuthUIActivityResultContract(),
            this::onSignInResult)
    }
// checks if result code is okay meaning signed in. and redirected to home page
    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        i(" onSignInResult %s",result.resultCode)
        if (result.resultCode == RESULT_OK) {
            i( "Signin successful!")
            startActivity(Intent(this,Home::class.java))
        }
        else if (result.resultCode == 0) finish()
        else if (result.idpResponse == null) {
            i(" Back Button")

            Toast.makeText(this,"Click again to Close App...",
                Toast.LENGTH_LONG).show()
        }
        else i(result.idpResponse!!.error)
    }


    override fun onBackPressed() {
        Toast.makeText(this,"Click again to Close App...",Toast.LENGTH_LONG).show()
        finish()
        super.onBackPressed()
    }
}
