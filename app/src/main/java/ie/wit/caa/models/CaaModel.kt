package ie.wit.caa.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CaaModel (var id: Long = 0,var name: String = "", var description: String = "", var type: String = "", var level: Int = 0) : Parcelable