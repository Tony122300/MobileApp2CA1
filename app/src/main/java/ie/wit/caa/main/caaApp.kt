package ie.wit.caa.main

import android.app.Application

import ie.wit.caa.models.CaaJSONStore
//import ie.wit.caa.models.CaaManager
import ie.wit.caa.models.Location
import timber.log.Timber

class caaApp : Application() {
//lateinit var crimeStore: CaaManager
lateinit var loc: Location
lateinit var caas: CaaJSONStore
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
       loc = Location(52.245696, -7.139102, 15f)
        caas = CaaJSONStore
        caas.initialize(this.applicationContext)
        //crimeStore = CaaManager(caas, loc)
        Timber.i("Crime Activity App Started")
    }
}
