package ie.wit.caa.main

import android.app.Application
import timber.log.Timber

class caaApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Timber.i("Crime Activity App Started")
    }
}
