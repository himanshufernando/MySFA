package tkhub.project.sfa.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "districts")
@Parcelize
data class Districts(
    @PrimaryKey
    var district_id : Int,
    var district_name: String,
    var district_active: Int,
    var isSelect : Boolean,

): Parcelable {
    constructor() : this(0, "", 1,false)
}