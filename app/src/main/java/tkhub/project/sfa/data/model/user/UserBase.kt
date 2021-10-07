package tkhub.project.sfa.data.model.user

data class UserBase(
    var error: Boolean,
    var message: String,
    var code:Int,
    var data : User
){
    constructor() : this(true, "",
        0, User()
    )

}