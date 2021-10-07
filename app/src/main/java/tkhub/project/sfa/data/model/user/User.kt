package tkhub.project.sfa.data.model.user

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "user")
@Parcelize
data class User (
    @PrimaryKey
    var user_id : Long,
    var user_uuid : String,
    var user_name : String,
    var user_email : String,
    var user_mobile : String,
    var user_emp_id : String,
    var user_is_active : Int,
    var user_password : String,
    var user_push_token : String,
    var user_profile : String,
    var user_level : Long



) : Parcelable {
    constructor() : this(0,"","","","","",0,"", "","",0)

}