package ie.wit.caa.models

import android.os.Parcelable
import android.widget.DatePicker
import com.google.firebase.database.Exclude
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CaaModel(
    var uid: String? = "",
    var name: String = "",
    var description: String = "",
    var type: String = "",
    var level: Int = 0,
    var date: String = "",
    var time: String = "",
    var profilepic: String = "",
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 0f,
    var email: String? = "tony@gmail.com") : Parcelable
{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "name" to name,
            "description" to description,
            "type" to type,
            "level" to level,
            "date" to date,
            "time" to time,
            "profilepic" to profilepic,
            "email" to email
        )
    }
}
@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable