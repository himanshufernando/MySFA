package tkhub.project.sfa.data.model.route

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "customerroute")
@Parcelize
data class Route(
    @PrimaryKey
    var customer_route_id: Long,
    var customer_route: String,
    var customer_route_code: String,
    var customer_route_active : Int,

) : Parcelable {
    constructor() : this(0, "", "",0)
}