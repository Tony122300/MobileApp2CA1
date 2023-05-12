package ie.wit.caa.main

import android.app.Application
import android.location.Location

//import ie.wit.caa.models.CaaJSONStore
//import ie.wit.caa.models.CaaManager
//import ie.wit.caa.models.CaaManager

import timber.log.Timber

class caaApp : Application() {
//lateinit var crimeStore: CaaManager
//lateinit var caas: CaaJSONStore
//lateinit var caas: CaaManager
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
//        caas = CaaJSONStore
//        caas.initialize(this.applicationContext)
      //  crimeStore = CaaManager(caas, loc)
        Timber.i("Crime Activity App Started")
    }
}
