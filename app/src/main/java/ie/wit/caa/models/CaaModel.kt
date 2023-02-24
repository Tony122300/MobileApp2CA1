package ie.wit.caa.models

import android.os.Parcelable
import android.widget.DatePicker
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CaaModel(
    var id: Long = 0,
    var name: String = "", var description: String = "", var type: String = "", var level: Int = 0, var date: String = "",var time: String = "",
    var lat: Double = 0.0, var lng: Double = 0.0, var zoom: Float = 0f) : Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable