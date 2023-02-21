package ie.wit.caa.main

import android.app.Application
import ie.wit.caa.models.CaaManager
import timber.log.Timber

class caaApp : Application() {
//lateinit var crimeStore: CaaManager
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
       // crimeStore = CaaManager()
        Timber.i("Crime Activity App Started")
    }
}
