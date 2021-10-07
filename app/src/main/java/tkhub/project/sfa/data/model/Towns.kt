package tkhub.project.sfa.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "towns")
@Parcelize
data class Towns(
    @PrimaryKey
    var id : Long,
    var district_id: Int,
    var name_en: String,
    var postcode : Int,
    var latitude: Double,
    var longitude: Double,
    var town_active : Int

): Parcelable {
    constructor() : this(0, 0, "",0,0.0,0.0,0)
}