package ie.wit.caa.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
//import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso
import ie.wit.caa.R
import ie.wit.caa.databinding.HomeBinding
import ie.wit.caa.databinding.NavHeaderMainBinding
import ie.wit.caa.firebase.FirebaseImageManager
import ie.wit.caa.firebaseui.FirebaseUIAuthManager
import ie.wit.caa.ui.auth.LoggedInViewModel
import ie.wit.caa.ui.auth.Login
import ie.wit.caa.ui.map.MapsViewModel
//import ie.wit.caa.ui.map.MapsViewModel
import ie.wit.caa.utils.*
import timber.log.Timber

class Home : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var homeBinding: HomeBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var loggedInViewModel: LoggedInViewModel
    private lateinit var navHeaderMainBinding: NavHeaderMainBinding
    private lateinit var headerView : View
    private lateinit var intentLauncher : ActivityResultLauncher<Intent>
    private val mapsViewModel :MapsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.content_home)
        homeBinding = HomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)
        drawerLayout = homeBinding.drawerLayout
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        loggedInViewModel = ViewModelProvider(this)[LoggedInViewModel::class.java]
        loggedInViewModel.liveFirebaseUser.value = FirebaseUIAuthManager.auth.currentUser

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(setOf(R.id.reportCrimeActivityFragment, R.id.listFragment,R.id.mapsFragment2), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        val navView = homeBinding.navView
        navView.setupWithNavController(navController)
        initNavHeader()
        if(checkLocationPermissions(this)) {
            mapsViewModel.updateCurrentLocation()
        }
    }

    public override fun onStart() {
        super.onStart()
//        loggedInViewModel = ViewModelProvider(this).get(LoggedInViewModel::class.java)
//        loggedInViewModel.liveFirebaseUser.observe(this, Observer { firebaseUser ->
//            if (firebaseUser != null)
//                updateNavHeader(firebaseUser)
//        })
//
//        loggedInViewModel.loggedOut.observe(this, Observer { loggedout ->
//            if (loggedout) {
//                startActivity(Intent(this, Login::class.java))
//            }
//        })


        Timber.i("EMAIL IS : %s", loggedInViewModel.liveFirebaseUser.value?.email)
        loggedInViewModel.liveFirebaseUser.observe(this) { firebaseUser ->
            if (firebaseUser != null)
                updateNavHeader(loggedInViewModel.liveFirebaseUser.value!!)
        }

        loggedInViewModel.loggedOut.observe(this) { loggedout ->
            if (loggedout) {
                startActivity(Intent(this, Login::class.java))
            }
        }
        registerImagePickerCallback()
    }

    private fun initNavHeader() {
        Timber.i("CAA Init Nav Header")
        headerView = homeBinding.navView.getHeaderView(0)
        navHeaderMainBinding = NavHeaderMainBinding.bind(headerView)
        navHeaderMainBinding.navHeaderImage.setOnClickListener {
            Toast.makeText(this,"Click To Change Image",Toast.LENGTH_SHORT).show()
            showImagePicker(intentLauncher)
        }

    }

    fun onThemeSwitchClicked(item: MenuItem) {
        val mode = if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.MODE_NIGHT_NO
        } else {
            AppCompatDelegate.MODE_NIGHT_YES
        }
        AppCompatDelegate.setDefaultNightMode(mode)
        recreate()
    }


    private fun updateNavHeader(currentUser: FirebaseUser) {
        var headerView = homeBinding.navView.getHeaderView(0)
        navHeaderMainBinding = NavHeaderMainBinding.bind(headerView)

        if(currentUser.email != null) {
            navHeaderMainBinding.navHeaderName.text = currentUser.displayName
            navHeaderMainBinding.navHeaderEmail.text = currentUser.email

            if(currentUser.photoUrl != null)
                Picasso.get().load(currentUser.photoUrl)
                    .resize(200, 200)
                    .transform(customTransformation())
                    .centerCrop()
                    .into(navHeaderMainBinding.navHeaderImage)
        }
        else
            navHeaderMainBinding.navHeaderEmail.text = currentUser.phoneNumber
    }

        override fun onSupportNavigateUp(): Boolean {
            val navController = findNavController(R.id.nav_host_fragment)
            return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
        }

    fun signOut(v: MenuItem) {
        loggedInViewModel.signOut()
        FirebaseUIAuthManager.auth.signOut()
        finish()
    }


    private fun registerImagePickerCallback() {
        intentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("CAA registerPickerCallback() ${readImageUri(result.resultCode, result.data).toString()}")
                            FirebaseImageManager
                                .updateUserImage(loggedInViewModel.liveFirebaseUser.value!!.uid,
                                    readImageUri(result.resultCode, result.data),
                                    navHeaderMainBinding.navHeaderImage,
                                    true)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (isPermissionGranted(requestCode, grantResults))
            mapsViewModel.updateCurrentLocation()
        else {
            // permissions denied, so use a default location
            mapsViewModel.currentLocation.value = Location("Default").apply {
                latitude = 52.245696
                longitude = -7.139102
            }
        }
        Timber.i("LOC : %s", mapsViewModel.currentLocation.value)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.nav_drawer_menu, menu)
        val switchItem = menu.findItem(R.id.theme_switch)
        val switchTextView = switchItem.actionView?.findViewById<TextView>(R.id.theme_switch_textview)
        switchTextView?.setOnClickListener { onThemeSwitchClicked(switchItem) }
        return true
    }


}