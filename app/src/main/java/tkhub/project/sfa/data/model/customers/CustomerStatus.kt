package tkhub.project.sfa.data.model.customers

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "customerstatus")
@Parcelize
data class CustomerStatus(
    @PrimaryKey
    var customer_status_id: Long,
    var customer_status: String,
    var customer_status_code: String,
    var customer_status_active : Int,

) : Parcelable {
    constructor() : this(0, "", "",0)
}