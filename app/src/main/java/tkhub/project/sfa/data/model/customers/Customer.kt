package tkhub.project.sfa.data.model.customers

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "customertb")
@Parcelize
data class Customer (
    @PrimaryKey
    var customer_id : Long,
    var customer_uuid: String,
    var customer_app_id: String,
    var customer_name : String,
    var customer_image : String,
    var customer_address : String,
    var customer_district : String,
    var customer_district_id : Int,
    var customer_area : String,
    var customer_town : String,
    var customer_town_id : Long,
    var customer_route : String,
    var customer_route_id : Long,
    var customer_status : String,
    var customer_status_id : Long,
    var customer_contact_number : String,
    var customer_email : String,
    var customer_br_number : String,
    var customer_owners_name : String,
    var customer_owners_contact_number : String,
    var customer_reg_number : String,
    var customer_user : Long,
    var customer_approved : Boolean,
    var customer_reject : Boolean,
    var customer_uploaded : Boolean,
    var customer_latitude : Double,
    var customer_longitude : Double,
    var customer_app_date: String,
    var customerIsExpand : Boolean,

): Parcelable  {

    constructor() : this(0,"","","","","","",0,"","",0,"",0,"",
        0,"","","","","","",0,false,
        false,false,0.0,0.0, "",false
        )
}