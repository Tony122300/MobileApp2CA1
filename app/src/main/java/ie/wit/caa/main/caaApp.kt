package ie.wit.caa.main

import android.app.Application
import ie.wit.caa.models.CaaMemStore
import timber.log.Timber

class caaApp : Application() {
lateinit var crimeStore: CaaMemStore
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        crimeStore = CaaMemStore()
        Timber.i("Crime Activity App Started")
    }
}
